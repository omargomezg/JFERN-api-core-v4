package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.persistence.model.constant.SaleOrderStatusEnum;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

public interface SaleOrderRepository extends MongoRepository<SaleOrderDocument, String>, SaleOrderRepositoryCustom {
    List<SaleOrderDocument> findByClient(UserDocument user);

    Page<SaleOrderDocument> findAllByClient(UserDocument user, Pageable pageable);

    List<SaleOrderDocument> findAllByStatus(SaleOrderStatusEnum status);

    Optional<SaleOrderDocument> findByClientAndStatus(UserDocument user, SaleOrderStatusEnum status);

}
