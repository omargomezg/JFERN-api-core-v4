package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.exception.AuthException;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.core.convert.ConversionService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@Log4j2
@RequiredArgsConstructor
public class JwtUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder bcryptEncoder;
    private final ConversionService conversionService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
        /*return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER"));*/
    }

    public UserDocument create(UserDocument user) {
        userRepository.findByEmail(user.getEmail()).ifPresent(u -> {
            log.error("User with email {} already exists", u.getEmail());
            throw new AuthException("Alguien mas usa este email, intente con otro.");
        });
        var encodedPassword = bcryptEncoder.encode(user.getPassword());
        user.setPassword(encodedPassword);
        return userRepository.save(user);
    }

    public UserDTO get(String id) {
        var user = userRepository.findById(id).orElseThrow();
        return conversionService.convert(user, UserDTO.class);
    }

    public UserDocument getByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow();
    }

    public void update(UserDTO userDTO) {
        var user = userRepository.findById(userDTO.getId()).orElseThrow();
        user.setTelephone(userDTO.getTelephone());
        user.setFullName(userDTO.getFullName());
        userRepository.save(user);
    }
}
