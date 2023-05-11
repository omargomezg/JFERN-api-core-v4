package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PlaceRepository extends MongoRepository<PlaceDocument, String> {
}
