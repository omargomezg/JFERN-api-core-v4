package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.model.constant.StatusPlaceEnum;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import com.southpurity.apicore.persistence.repository.SaleOrderRepository;
import com.southpurity.apicore.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PlaceServiceImpl implements PlaceService {

    private final ProductRepository productRepository;
    private final PlaceRepository placeRepository;
    private final SaleOrderRepository saleOrderRepository;
    private final MongoTemplate mongoTemplate;


    @Override
    public List<PlaceDocument> findAll(StatusPlaceEnum statusPlaceEnum) {
        Query query = new Query();
        if (statusPlaceEnum != null) {
            query.addCriteria(Criteria.where("status").is(statusPlaceEnum));
        }

        var places = mongoTemplate.find(query, PlaceDocument.class);
        places.forEach(place -> {
            var total = productRepository.countProductDocumentByPlace(place);
            place.setPadlocks(total);
        });
        return places;
    }

    @Override
    public PlaceDocument update(PlaceDocument placeDocument) {
        return placeRepository.save(placeDocument);
    }

    @Override
    public Optional<PlaceDocument> findById(String id) {
        return placeRepository.findById(id);
    }

    @Override
    public boolean releaseProduct(String place, String product) {
        PlaceDocument placeDocument = placeRepository.findById(place).orElseThrow(()-> new RuntimeException("Place not found"));
        ProductDocument productDocument = productRepository.findById(product).orElseThrow(()-> new RuntimeException("Product not found"));
        List<SaleOrderDocument> orders = saleOrderRepository.findAllByProductsId(product);
        if (orders.isEmpty()) {
            productDocument.setStatus(OrderStatusEnum.AVAILABLE);
            productRepository.save(productDocument);
            return true;
        }
        return false;
    }

}
