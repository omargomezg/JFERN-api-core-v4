package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.repository.PlaceRepository;
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
    private final PlaceRepository placeRepository;

    public ProfileResponse get() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        final UserDetails user = (UserDetails) authentication.getPrincipal();
        var userDocumento = userRepository.findByEmail(user.getUsername()).orElseThrow();
        String fillAddress = "";
        var place = placeRepository.findById(userDocumento.getAddresses().get(0).getPlaceId());
        if (place.isPresent()) {
            fillAddress = String.format("%s, %s",
                    place.get().getAddress(), userDocumento.getAddresses().get(0).getAddress());
        }
        return ProfileResponse.builder()
                .id(userDocumento.getId())
                .rut(userDocumento.getRut())
                .email(user.getUsername())
                .telephone(userDocumento.getTelephone())
                .fullName(userDocumento.getFullName())
                .address(fillAddress)
                .build();
    }

    public UserDocument update(ProfileResponse profile) {
        var user = userRepository.findById(profile.getId()).orElseThrow();
        user.setTelephone(profile.getTelephone());
        user.setFullName(profile.getFullName());
        return userRepository.save(user);
    }
}
