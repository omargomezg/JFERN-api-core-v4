package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.PlaceDTO;
import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public List<PlaceDTO> getAllPlaces() {
        return placeRepository.findAll().stream().map(this::placeDocumentToDTO)
                .collect(Collectors.toList());
    }

    public PlaceDTO savePlace(PlaceDocument place) {
        var placeFound = placeRepository.findById(place.getId());
        if (placeFound.isEmpty()) {
            place.setId(null);
        }
        return placeDocumentToDTO(placeRepository.save(place));
    }

    public List<UserDTO> getAllUsers(RoleEnum role) {
        return userRepository.findAllByRole(role).stream()
                .map(user -> UserDTO.builder()
                        .id(user.getId())
                        .rut(user.getRut())
                        .fullName(user.getFullName())
                        .telephone(user.getTelephone())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .status(user.getStatus())
                        //.fullAddress("")
                        .build())
                .collect(Collectors.toList());

    }

    protected List<String> getAddress(UserDocument user) {
        return new ArrayList<>();
    }

    protected PlaceDTO placeDocumentToDTO(PlaceDocument place) {
        return PlaceDTO.builder()
                .id(place.getId())
                .address(place.getAddress())
                .availableStock(place.getAvailableStock())
                .build();
    }

}
