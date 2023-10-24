package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.dto.ProductFilter;
import com.southpurity.apicore.exception.ProductException;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.persistence.repository.UserRepository;
import com.southpurity.apicore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;
    private final UserRepository userRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final MongoTemplate mongoTemplate;

    @Override
    public Page<ProductDocument> getAll(ProductFilter filter, Pageable pageable) {
        Query query = new Query().with(pageable);
        if (filter.getPlaceId() != null) {
            query.addCriteria(Criteria.where("place").is(new ObjectId(filter.getPlaceId())));
        }
        if (filter.getStatus() != null) {
            query.addCriteria(Criteria.where("status").is(filter.getStatus()));
        }
        var products = mongoTemplate.find(query, ProductDocument.class);
        return PageableExecutionUtils.getPage(
                products,
                pageable,
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), ProductDocument.class));
    }

    @Override
    public Page<ProductDocument> getAllAvailable(String placeId, OrderStatusEnum status, Pageable pageable) {
        var place = placeRepository.findById(placeId).orElseThrow();
        return productRepository.findAllByPlaceAndStatus(place, status, pageable);
    }

    @Override
    public ProductDocument create(ProductDTO productDTO) {
        var place = placeRepository.findById(productDTO.getPlace()).orElseThrow();
        productRepository.findByPlaceAndLockNumber(place, productDTO.getLockNumber()).ifPresent(product -> {
            throw new ProductException("Ya existe un producto con el número de candado " + productDTO.getLockNumber());
        });
        return productRepository.save(ProductDocument.builder()
                .shortName("Bidón de 20 litros")
                .place(place)
                .lockNumber(productDTO.getLockNumber())
                .padlockKey(productDTO.getPadlockKey())
                .build());
    }

    @Override
    public ProductDocument update(ProductDocument product) {
        return null;
    }

    @Override
    public void delete(String id) {
        productRepository.deleteById(id);
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
