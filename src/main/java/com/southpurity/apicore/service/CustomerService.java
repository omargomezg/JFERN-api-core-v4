package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.AvailableDrums;
import com.southpurity.apicore.dto.customer.CustomerPlaceRequest;
import com.southpurity.apicore.dto.customer.MyAddressResponse;
import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.persistence.model.AddressDocument;
import com.southpurity.apicore.persistence.model.Price;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.PriceTypeEnum;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;

    public List<MyOrderResponseDTO> getMyOrders() {
        return new ArrayList<>();
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

    public List<AvailableDrums> getAvailableWaterDrums(String place) {
        List<AvailableDrums> availableDrums = new ArrayList<>();
        var placeDocument = placeRepository.findById(place).orElseThrow();
        List<ProductDocument> bottles = productRepository.findAllByPlaceAndStatus(placeDocument, OrderStatusEnum.AVAILABLE);
        bottles.forEach(bottle -> {
            if (availableDrums.stream().anyMatch(item -> Objects.equals(item.getDescription(), bottle.getShortName()))) {
                availableDrums.stream()
                        .filter(item -> Objects.equals(item.getDescription(), bottle.getShortName()))
                        .findFirst()
                        .ifPresent(item -> item.setAvailable(item.getAvailable() + 1));
            } else {
                availableDrums.add(AvailableDrums.builder()
                        .available(1)
                        .description(bottle.getShortName())
                        .refillPrice(getAmount(bottle.getPrices(), PriceTypeEnum.REFILL))
                        .bottlePrice(getAmount(bottle.getPrices(), PriceTypeEnum.BOTTLE))
                        .build());
            }
        });
        return availableDrums;
    }

    private int getAmount(Set<Price> prices, PriceTypeEnum type) {
        if(prices == null) {
            return PriceTypeEnum.BOTTLE.equals(type) ? 4500 : 2500;
        }
        int defaultAmount = PriceTypeEnum.BOTTLE.equals(type) ? 4500 : 2500;
        return prices.stream()
                .filter(price -> price.getIdentifier().equals(type))
                .findFirst()
                .map(Price::getAmount)
                .orElse(defaultAmount);
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

    protected MyOrderResponseDTO orderDocumentToDTO(ProductDocument order) {
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
