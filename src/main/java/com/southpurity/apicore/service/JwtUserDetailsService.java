package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
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
    private final PlaceRepository placeRepository;
    private final PasswordEncoder bcryptEncoder;
    private final ConversionService conversionService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return (UserDetails) userRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + email));
        /*return new org.springframework.security.core.userdetails.User(user.getEmail(), user.getPassword(),
                AuthorityUtils.createAuthorityList("ROLE_USER"));*/
    }

    public UserDocument create(UserDocument user) {
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
