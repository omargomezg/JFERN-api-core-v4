package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.persistence.model.AddressDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
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
        var address = new StringBuilder();
        userDocumento.getAddresses().stream()
                .filter(AddressDocument::getIsPrincipal)
                .findFirst()
                .ifPresent(place -> address
                        .append(place.getPlace().getAddress())
                        .append(", ")
                        .append(place.getAddress())
                        .append(". ")
                        .append(place.getPlace().getCountry()));
        return ProfileResponse.builder()
                .id(userDocumento.getId())
                .rut(userDocumento.getRut())
                .email(user.getUsername())
                .telephone(userDocumento.getTelephone())
                .role(userDocumento.getRole())
                .fullName(userDocumento.getFullName())
                .address(address.toString())
                .build();
    }

    public UserDocument update(ProfileResponse profile) {
        var user = userRepository.findById(profile.getId()).orElseThrow();
        user.setTelephone(profile.getTelephone());
        user.setFullName(profile.getFullName());
        return userRepository.save(user);
    }
}
