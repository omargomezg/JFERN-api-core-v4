package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.producttype.BottleDocument;
import com.southpurity.apicore.persistence.model.producttype.ProductTypeDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
    public interface BottleRepository extends MongoRepository<BottleDocument, String> {

        Optional<BottleDocument> findByShortName(String shortName);

    }

