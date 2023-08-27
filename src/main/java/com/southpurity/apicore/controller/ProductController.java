package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.ProductFilter;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<Page<ProductDocument>> getAllProducts(ProductFilter filter,
                                                                Pageable pageable) {
        return ResponseEntity.ok(productService.getAll(filter, pageable));
    }

    @PutMapping
    public ResponseEntity<ProductDocument> updateProduct(ProductDocument product) {
        return ResponseEntity.ok(productService.update(product));
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteProduct(String id) {
        productService.delete(id);
        return ResponseEntity.ok().build();
    }

}
