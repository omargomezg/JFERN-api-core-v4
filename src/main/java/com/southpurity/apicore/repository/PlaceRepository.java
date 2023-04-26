package com.southpurity.apicore.repository;

import com.southpurity.apicore.model.PlaceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends MongoRepository<PlaceDocument, String> {
}
