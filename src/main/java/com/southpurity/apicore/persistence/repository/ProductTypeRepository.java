package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.ProductType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

    @Repository
    public interface ProductTypeRepository extends MongoRepository<ProductType, String> {
    }

