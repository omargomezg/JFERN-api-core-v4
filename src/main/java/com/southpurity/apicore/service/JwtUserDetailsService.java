package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {



    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        var user = userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
        return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER"));
    }

    public UserDocument save(UserDTO user) {
        var newUser = new UserDocument();
        newUser.setEmail(user.getEmail());
        newUser.setPassword(bcryptEncoder.encode(user.getPassword()));
        return userRepository.save(newUser);
    }
}
