package com.southpurity.apicore.persistence.repository;

import com.southpurity.apicore.dto.SaleOrderFilter;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import org.springframework.data.domain.Page;

import java.util.List;

public interface SaleOrderRepositoryCustom {
    Page<SaleOrderDocument> findAll(SaleOrderFilter filter);
}
