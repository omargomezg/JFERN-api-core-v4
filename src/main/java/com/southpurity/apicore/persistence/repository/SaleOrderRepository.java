package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SaleOrderRepository extends MongoRepository<SaleOrderDocument, String> {
    List<SaleOrderDocument> findByClient(UserDocument user);

    Page<SaleOrderDocument> findAllByClient(UserDocument user, Pageable pageable);

    List<SaleOrderDocument> findAllByStatus(SaleOrderStatusEnum status);

}
