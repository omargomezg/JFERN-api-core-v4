package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProfileService {

    private final UserRepository userRepository;

    public ProfileResponse getProfile() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails user = (UserDetails) authentication.getPrincipal();
        var userDocumento = userRepository.findByEmail(user.getUsername()).orElseThrow();
        return ProfileResponse.builder()
                .rut(userDocumento.getRut())
                .fullName(userDocumento.getFullName())
                .role(userDocumento.getRole())
                .build();
    }
}
