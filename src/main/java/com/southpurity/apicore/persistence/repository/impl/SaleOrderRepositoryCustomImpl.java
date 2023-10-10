package com.southpurity.apicore.persistence.repository.impl;

import com.southpurity.apicore.dto.SaleOrderFilter;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.repository.SaleOrderRepositoryCustom;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class SaleOrderRepositoryCustomImpl implements SaleOrderRepositoryCustom {

    private final MongoTemplate mongoTemplate;

    @Autowired
    public SaleOrderRepositoryCustomImpl(MongoTemplate mongoTemplate) {
        this.mongoTemplate = mongoTemplate;
    }

    @Override
    public Page<SaleOrderDocument> findAll(SaleOrderFilter filter) {
        Query query  = new Query().with(filter.getPageable());
        if (filter.getUserId() != null) {
            query.addCriteria(Criteria.where("client").is(new ObjectId(filter.getUserId())));
        }
        return PageableExecutionUtils.getPage(
                mongoTemplate.find(query, SaleOrderDocument.class),
                filter.getPageable(),
                () -> mongoTemplate.count(Query.of(query).limit(-1).skip(-1), SaleOrderDocument.class));
    }
}
