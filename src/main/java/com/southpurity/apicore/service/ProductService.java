package com.southpurity.apicore.service;

import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.dto.ProductFilter;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.constant.OrderStatusEnum;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ProductService {

    Page<ProductDocument> getAll(ProductFilter filter, Pageable pageable);

    Page<ProductDocument> getAllAvailable(String placeId, OrderStatusEnum status, Pageable pageable);

    ProductDocument create(ProductDTO order);

    ProductDocument update(ProductDocument product);

    void delete(String id);

    void cancelOrder(String userId);


}
