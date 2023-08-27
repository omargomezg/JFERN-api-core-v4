package com.southpurity.apicore.persistence.repository;


import com.southpurity.apicore.persistence.model.ProductDocument;

import java.util.List;

public interface ProductRepositoryCustom {

    List<ProductDocument> markAsTaken(List<String> ids, String place);

    void markAsAvailable(List<String> ids, String place);

    List<ProductDocument> markAsTaken(int quantity, String place);
}
