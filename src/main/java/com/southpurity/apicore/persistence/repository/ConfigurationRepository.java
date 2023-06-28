package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ConfigurationRepository extends MongoRepository<ConfigurationDocument, String> {
    Optional<ConfigurationDocument> findBySiteName(String siteName);
}
