package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.CartRequest;
import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.exception.SaleOrderException;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.CurrencyEnum;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.ItemDocument;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.ConfigurationRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
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
    public Page<ProductDocument> getAllAvailable(String placeId, Pageable pageable) {
        var place = placeRepository.findById(placeId).orElseThrow();
        return productRepository.findAllByPlaceAndStatus(place, OrderStatusEnum.AVAILABLE, pageable);
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
        var quantity = cartRequest.stream().mapToLong(CartRequest::getQuantity).sum();
        UserDocument user = userRepository.findById(profile.getId()).orElseThrow();
        PlaceDocument place = placeRepository.findById(addressId).orElseThrow();
        var products = productRepository.findAllByPlaceAndStatus(place, OrderStatusEnum.AVAILABLE, Pageable.ofSize((int) quantity));
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
                .serial(new Date().getTime())
                .products(products.getContent())
                .items(items)
                .build();
        return saleOrderRepository.save(saleOrder);
    }

    @Override
    public void cancelOrder(String userId) {
        var user = userRepository.findById(userId).orElseThrow();
        var saleOrder = saleOrderRepository.findByClientAndStatus(user, SaleOrderStatusEnum.PENDING).orElseThrow();
        saleOrder.getProducts().forEach(product -> {
            product.setStatus(OrderStatusEnum.AVAILABLE);
            productRepository.save(product);
        });
        saleOrder.setStatus(SaleOrderStatusEnum.UNKNOWN);
        saleOrderRepository.save(saleOrder);
    }
}
