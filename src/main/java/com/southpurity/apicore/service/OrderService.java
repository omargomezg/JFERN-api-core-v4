package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.administrator.OrderDTO;
import com.southpurity.apicore.persistence.model.OrderDocument;
import com.southpurity.apicore.persistence.repository.OrderRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PlaceRepository placeRepository;

    public OrderDocument create(OrderDTO order) {
        var place = placeRepository.findById(order.getPlace()).orElseThrow();

        return orderRepository.save(OrderDocument.builder()
                .place(place)
                .lockNumber(order.getLockNumber())
                .padlockKey(order.getPadlockKey())
                .build());
    }

}
