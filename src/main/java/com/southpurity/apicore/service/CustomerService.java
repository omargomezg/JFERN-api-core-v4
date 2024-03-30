package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.AvailableDrums;
import com.southpurity.apicore.dto.customer.CustomerPlaceRequest;
import com.southpurity.apicore.dto.customer.MyAddressResponse;
import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.persistence.model.AddressDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.repository.BottleRepository;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
@Log4j2
@RequiredArgsConstructor
public class CustomerService {
    private static final Logger LOGGER = LoggerFactory.getLogger(CustomerService.class);
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;
    private final ConfigurationRepository configurationRepository;
    private final BottleRepository bottleRepository;

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

    public List<AvailableDrums> getAvailableBottles(String place) {
        List<AvailableDrums> response = new ArrayList<>();
        PlaceDocument placeDocument = placeRepository.findById(place).orElseThrow();
        var products = productRepository.findAllByPlaceAndStatus(placeDocument, OrderStatusEnum.AVAILABLE);
        products.stream()
                .filter(distinctByKey(ProductDocument::getProductType))
                .forEach(product -> response.add(toAvailableDrums(product)));
        response.forEach(availableDrums -> {
            availableDrums.setAvailable(products.stream()
                    .filter(product -> product.getProductType().getShortName().equals(availableDrums.getDescription()))
                    .toList().size());
        });
        return response;
    }

    private AvailableDrums toAvailableDrums(ProductDocument product) {
        return AvailableDrums.builder()
                .description(product.getProductType().getShortName())
                .price(product.getProductType().getPriceRecharge())
                .priceWithDrum(product.getProductType().getPriceDrum())
                .build();
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

    private <T> Predicate<T> distinctByKey(Function<? super T, ?> keyExtractor) {
        Map<Object, Boolean> seen = new ConcurrentHashMap<>();
        return t -> seen.putIfAbsent(keyExtractor.apply(t), Boolean.TRUE) == null;
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
