package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.producttype.BottleDocument;
import com.southpurity.apicore.persistence.repository.BottleRepository;
import com.southpurity.apicore.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductTypeServiceImpl implements ProductTypeService<BottleDocument> {

    private final BottleRepository bottleRepository;

    @Override
    public List<BottleDocument> getAll() {
        return bottleRepository.findAll();
    }

    @Override
    public BottleDocument create(@Valid BottleDocument productTypeDocument) {
        bottleRepository.findByShortName(productTypeDocument.getShortName()).ifPresent(productType -> {
            throw new RuntimeException("Ya existe un tipo de producto con el nombre " + productTypeDocument.getShortName());
        });
        return bottleRepository.save(productTypeDocument);
    }
}
