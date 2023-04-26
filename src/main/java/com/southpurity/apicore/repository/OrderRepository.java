package com.southpurity.apicore.repository;

import com.southpurity.apicore.controller.CustomerController;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends MongoRepository<CustomerController, String> {
}
