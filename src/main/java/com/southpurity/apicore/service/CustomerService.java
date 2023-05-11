package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.customer.CustomerPlaceRequest;
import com.southpurity.apicore.dto.customer.MyAddressResponse;
import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.persistence.model.AddressDocument;
import com.southpurity.apicore.persistence.model.OrderDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.repository.OrderRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PlaceRepository placeRepository;

    public List<MyOrderResponseDTO> getMyOrders() {
        return getMyData()
                .getOrders().stream()
                .map(this::orderDocumentToDTO).collect(Collectors.toList());
    }

    public List<MyAddressResponse> getMyPlaces() {
        var user = getMyData();
        return user.getAddresses().stream()
                .map(this::placeDocumentToMyAddressResponse)
                .collect(Collectors.toList());
    }

    private MyAddressResponse placeDocumentToMyAddressResponse(AddressDocument place) {
        return MyAddressResponse.builder()
                .id(place.getPlace().getId())
                .fullAddress(place.fullAddress())
                .isPrimary(place.getIsPrincipal())
                .build();
    }

    public int getAvailableWaterDrums(String place) {
        var placeDocument = placeRepository.findById(place).orElseThrow();
        return orderRepository.findAllByPlaceAndStatus(placeDocument, OrderStatusEnum.AVAILABLE)
                .size();
    }

    public void addPlace(CustomerPlaceRequest customerPlace) {
        var place = placeRepository.findById(customerPlace.getIdPlace()).orElseThrow();
        var user = getMyData();
        user.getAddresses().add(
                AddressDocument.builder()
                        .place(place)
                        .address(customerPlace.getAddress())
                        .isPrincipal(customerPlace.getIsPrincipal())
                        .build());
        userRepository.save(user);
    }

    protected MyOrderResponseDTO orderDocumentToDTO(OrderDocument order) {
        return MyOrderResponseDTO.builder()
                .id(order.getId())
                .address(String.format("%, %", order.getPlace().getAddress(), order.getPlace().getCountry()))
                .date(order.getCreatedDate())
                .build();
    }

    private UserDocument getMyData() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        LOGGER.info("Getting orders for {}", authentication.getName());
        return userRepository.findByEmail(authentication.getName()).orElseThrow();
    }

}
