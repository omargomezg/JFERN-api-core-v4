package com.southpurity.apicore.persistence.repository.impl;

import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import com.southpurity.apicore.persistence.repository.ProductRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class ProductRepositoryCustomImpl implements ProductRepositoryCustom {

    public static final String STATUS = "status";
    public static final String PLACE = "place";
    private final MongoTemplate mongoTemplate;

    @Autowired
    public ProductRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public List<ProductDocument> markAsTaken(List<String> ids, String place) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        query.addCriteria(Criteria.where(PLACE).is(new ObjectId(place)));
        var products = mongoTemplate.find(query, ProductDocument.class);
        products.forEach(productDocument -> {
            productDocument.setStatus(OrderStatusEnum.TAKEN);
            mongoTemplate.save(productDocument);
        });
        return getAll(0, place, OrderStatusEnum.TAKEN, ids);
    }

    @Override
    public void markAsAvailable(List<String> ids, String place) {
        Query query = new Query();
        query.addCriteria(Criteria.where("id").in(ids));
        query.addCriteria(Criteria.where(PLACE).is(new ObjectId(place)));
        var products = mongoTemplate.find(query, ProductDocument.class);
        products.forEach(productDocument -> {
            productDocument.setStatus(OrderStatusEnum.AVAILABLE);
            mongoTemplate.save(productDocument);
        });
    }

    @Override
    public List<ProductDocument> markAsTaken(int limit, String place) {
        Query query = new Query();
        query.addCriteria(Criteria.where(STATUS).is(OrderStatusEnum.AVAILABLE));
        query.addCriteria(Criteria.where(PLACE).is(new ObjectId(place)));
        query.with(Sort.by(Sort.Direction.ASC, "createdDate"));
        query.limit(limit);
        var result = mongoTemplate.find(query, ProductDocument.class);
        result.forEach(productDocument -> {
            productDocument.setStatus(OrderStatusEnum.TAKEN);
            mongoTemplate.save(productDocument);
        });
        return getAll(limit, place, OrderStatusEnum.TAKEN, new ArrayList<>());
    }

    private List<ProductDocument> getAll(int limit, String place, OrderStatusEnum status,
                                         List<String> ids) {
        Query query = new Query();
        query.addCriteria(Criteria.where(STATUS).is(status));
        query.addCriteria(Criteria.where(PLACE).is(new ObjectId(place)));
        query.with(Sort.by(Sort.Direction.ASC, "createdDate"));
        if (!ids.isEmpty()) {
            query.addCriteria(Criteria.where("id").in(ids));
        }
        if (limit > 0) {
            query.limit(limit);
        }
        return mongoTemplate.find(query, ProductDocument.class);
    }
}
