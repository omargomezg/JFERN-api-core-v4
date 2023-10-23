package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends MongoRepository<ProductDocument, String>, ProductRepositoryCustom {

    Page<ProductDocument> findAllByPlaceAndStatus(PlaceDocument placeDocument, OrderStatusEnum orderStatusEnum,
                                                  Pageable pageable);

    List<ProductDocument> findAllByPlaceAndStatus(PlaceDocument placeDocument, OrderStatusEnum orderStatusEnum);

    short countProductDocumentByPlace(PlaceDocument placeDocument);

    Optional<ProductDocument> findByPlaceAndLockNumber(PlaceDocument place, String lockNumber);

}
