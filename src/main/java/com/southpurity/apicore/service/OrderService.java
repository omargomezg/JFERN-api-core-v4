package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.OrderDTO;
import com.southpurity.apicore.dto.PageDTO;
import com.southpurity.apicore.persistence.model.OrderDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.repository.OrderRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PlaceRepository placeRepository;
    private final ModelMapper modelMapper;

    public PageDTO<OrderDTO> getAllAvailable(String placeId, Pageable pageable) {
        var place = placeRepository.findById(placeId).orElseThrow();
        var orders = orderRepository.findAllByPlaceAndStatus(place, OrderStatusEnum.AVAILABLE, pageable)
                .map(order -> modelMapper.map(order, OrderDTO.class));
        return new PageDTO<>(orders .getContent(), orders.getPageable(), orders.getTotalElements());
    }

    public OrderDocument create(OrderDTO order) {
        var place = placeRepository.findById(order.getPlace()).orElseThrow();

        return orderRepository.save(OrderDocument.builder()
                .place(place)
                .lockNumber(order.getLockNumber())
                .padlockKey(order.getPadlockKey())
                .build());
    }

}
