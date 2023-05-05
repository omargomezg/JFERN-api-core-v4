package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.customer.CustomerPlaceRequest;
import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.model.AddressDocument;
import com.southpurity.apicore.model.OrderDocument;
import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.repository.OrderRepository;
import com.southpurity.apicore.repository.PlaceRepository;
import com.southpurity.apicore.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PlaceRepository placeRepository;

    public List<MyOrderResponseDTO> getMyOrders() {
        return getMyData().getOrders().stream().map(this::orderDocumentToDTO).collect(Collectors.toList());
    }

    public List<PlaceDocument> getMyPlaces() {
        var user = getMyData();
        if (user.getAddresses() == null){
            return new ArrayList<>();
        }
        return user.getAddresses().stream()
                .filter(Objects::nonNull)
                .map(address -> placeRepository.findById(address.getPlaceId()).orElseThrow())
                .collect(Collectors.toList());
    }

    public void addPlace(CustomerPlaceRequest customerPlace){
        placeRepository.findById(customerPlace.getIdPlace()).orElseThrow();
        var user = getMyData();
        user.getAddresses().add(AddressDocument.builder()
                        .placeId(customerPlace.getIdPlace())
                        .address(customerPlace.getAddress())
                .build());
        userRepository.save(user);
    }

    protected MyOrderResponseDTO orderDocumentToDTO(OrderDocument order) {
        return MyOrderResponseDTO.builder().id(order.getId()).correlative(order.getCorrelative()).build();
    }

    private UserDocument getMyData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Getting orders for {}", authentication.getName());
        return userRepository.findByEmail(authentication.getName()).orElseThrow();
    }

}
