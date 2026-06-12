package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.dto.SaleOrderFilter;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import org.springframework.data.domain.Page;

import java.util.Optional;

public interface SaleOrderRepositoryCustom {
    Page<SaleOrderDocument> findAll(SaleOrderFilter filter);
    Optional<SaleOrderDocument> findByToken(String token);
}
