package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.administrator.PlaceResponseDTO;
import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.model.constant.RoleEnum;
import com.southpurity.apicore.repository.PlaceRepository;
import com.southpurity.apicore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AdministratorService {

    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;

    public List<PlaceResponseDTO> getAllPlaces() {
        return placeRepository.findAll().stream().map(this::placeDocumentToDTO)
                .collect(Collectors.toList());
    }

    public PlaceResponseDTO savePlace(PlaceDocument place) {
        var placeFound = placeRepository.findById(place.getId());
        if (placeFound.isEmpty()) {
            place.setId(null);
        }
        return placeDocumentToDTO(placeRepository.save(place));
    }

    public List<UserDocument> getAllUsers(RoleEnum role) {
        return userRepository.findAllByRole(role);
    }

    protected PlaceResponseDTO placeDocumentToDTO(PlaceDocument place) {
        return PlaceResponseDTO.builder()
                .id(place.getId())
                .address(place.getAddress())
                .build();
    }

}
