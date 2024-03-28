package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.persistence.model.producttype.BottleDocument;
import com.southpurity.apicore.persistence.repository.BottleRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductTypeServiceImplTest {

    @InjectMocks
    private ProductTypeServiceImpl productTypeService;

    @Mock
    private BottleRepository bottleRepository;

    @Test
    void create() {
        BottleDocument bottleDocument = new BottleDocument();
        bottleDocument.setShortName(null);
        bottleDocument.setStatus(true);

        when(bottleRepository.save(any())).thenReturn(bottleDocument);

        var response = productTypeService.create(bottleDocument);
        assertNotNull(response);
    }
}
