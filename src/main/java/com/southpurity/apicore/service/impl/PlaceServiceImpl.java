package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.PlaceDTO;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.repository.OrderRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final PlaceRepository placeRepository;
    private final OrderRepository orderRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<PlaceDTO> findAll() {
        return placeRepository.findAll().stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    protected PlaceDTO convertToDTO(PlaceDocument place) {
        var placeDTO = modelMapper.map(place, PlaceDTO.class);
        placeDTO.setPadlocks(orderRepository.findAllByPlaceAndStatus(place, OrderStatusEnum.AVAILABLE).size());
        return placeDTO;
    }

}
