package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.KangooJumps;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface KangooJumpsRepository extends MongoRepository<KangooJumps, String> {
}
