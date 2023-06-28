package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.CartRequest;
import com.southpurity.apicore.dto.PageDTO;
import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.exception.SaleOrderException;
import com.southpurity.apicore.persistence.model.saleorder.ItemDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final ConfigurationRepository configurationRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final ModelMapper modelMapper;

    @Override
    public PageDTO<ProductDTO> getAllAvailable(String placeId, Pageable pageable) {
        var place = placeRepository.findById(placeId).orElseThrow();
        var orders = productRepository.findAllByPlaceAndStatus(place, OrderStatusEnum.AVAILABLE, pageable)
                .map(order -> modelMapper.map(order, ProductDTO.class));
        return new PageDTO<>(orders.getContent(), orders.getPageable(), orders.getTotalElements());
    }

    @Override
    public ProductDocument create(ProductDTO productDTO) {
        var configuration = configurationRepository.findBySiteName("southpurity").orElseThrow();
        var place = placeRepository.findById(productDTO.getPlace()).orElseThrow();
        return productRepository.save(ProductDocument.builder()
                .shortName("Bidón de 20 litros")
                .place(place)
                .lockNumber(productDTO.getLockNumber())
                .padlockKey(productDTO.getPadlockKey())
                .build());
    }

    @Transactional
    @Override
    public SaleOrderDocument takeOrder(String addressId, List<CartRequest> cartRequest, ProfileResponse profile) {
        var quantity = cartRequest.stream().mapToInt(CartRequest::getQuantity).sum();
        UserDocument user = userRepository.findById(profile.getId()).orElseThrow();
        PlaceDocument place = placeRepository.findById(addressId).orElseThrow();
        var products = productRepository.findAllByPlaceAndStatus(place, OrderStatusEnum.AVAILABLE, Pageable.ofSize(quantity));
        if (products.getContent().size() != quantity) {
            throw new SaleOrderException("No hay productos suficientes.");
        }
        products.forEach(product -> {
            product.setStatus(OrderStatusEnum.TAKEN);
            productRepository.save(product);
        });
        List<ItemDocument> items = new ArrayList<>();
        cartRequest.forEach(cart -> items.add(ItemDocument.builder()
                .price(cart.getPrice())
                .name(cart.getDescription())
                .quantity(cart.getQuantity())
                .money(CurrencyEnum.CLP)
                .build()));
        SaleOrderDocument saleOrder = SaleOrderDocument.builder()
                .client(user)
                .products(products.getContent())
                .items(items)
                .build();
        return saleOrderRepository.save(saleOrder);
    }
}
