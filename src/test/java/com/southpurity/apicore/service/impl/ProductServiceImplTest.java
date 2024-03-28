package com.southpurity.apicore.service.impl;

import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.exception.ProductException;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.producttype.BottleDocument;
import com.southpurity.apicore.persistence.repository.BottleRepository;
import com.southpurity.apicore.persistence.repository.PlaceRepository;
import com.southpurity.apicore.persistence.repository.ProductRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.NoSuchElementException;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @InjectMocks
    ProductServiceImpl productService;

    @Mock
    PlaceRepository placeRepository;

    @Mock
    ProductRepository productRepository;

    @Mock
    BottleRepository bottleRepository;

    @Test
    void create_success() {
        ProductDTO productDTO = new ProductDTO();
        PlaceDocument placeDocument = mock(PlaceDocument.class);

        when(placeRepository.findById(productDTO.getPlace())).thenReturn(Optional.of(placeDocument));
        when(bottleRepository.findById(productDTO.getProductType())).thenReturn(Optional.of(mock(BottleDocument.class)));
        when(productRepository.save(any(ProductDocument.class))).thenReturn(mock(ProductDocument.class));

        ProductDocument product = productService.create(productDTO);
        Assertions.assertEquals(productDTO.getLockNumber(), product.getLockNumber());
    }

    @Test
    void create_place_not_exists() {
        ProductDTO productDTO = new ProductDTO();

        when(placeRepository.findById(productDTO.getPlace())).thenReturn(Optional.empty());

        Assertions.assertThrows(NoSuchElementException.class, () -> productService.create(productDTO));
    }

    @Test
    void create_bottle_already_exists() {
        ProductDTO productDTO = new ProductDTO();
        PlaceDocument placeDocument = mock(PlaceDocument.class);
        ProductDocument productDocument = mock(ProductDocument.class);

        when(placeRepository.findById(productDTO.getPlace())).thenReturn(Optional.of(placeDocument));
        when(bottleRepository.findById(productDTO.getProductType())).thenReturn(Optional.of(mock(BottleDocument.class)));
        when(productRepository.findByPlaceAndLockNumber(placeDocument, productDTO.getLockNumber())).thenReturn(Optional.of(productDocument));

        Assertions.assertThrows(ProductException.class, () -> productService.create(productDTO));
    }
}
