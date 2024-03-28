package com.southpurity.apicore.service;

import com.southpurity.apicore.persistence.model.producttype.ProductTypeDocument;

import java.util.List;

public interface ProductTypeService<T> {

    List<T> getAll();

    T create(T productType);

}
