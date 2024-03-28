package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.ProductFilter;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.persistence.model.producttype.BottleDocument;
import com.southpurity.apicore.persistence.model.producttype.ProductTypeDocument;
import com.southpurity.apicore.persistence.repository.BottleRepository;
import com.southpurity.apicore.service.ProductService;
import com.southpurity.apicore.service.ProductTypeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final ProductTypeService<BottleDocument> productTypeService;

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

    @GetMapping("/type")
    public ResponseEntity<List<BottleDocument>> getAllProductsByType() {
        return ResponseEntity.ok(productTypeService.getAll());
    }

    @PostMapping("/type")
    public ResponseEntity<BottleDocument> createProduct(@Valid @RequestBody BottleDocument productType) {
        return ResponseEntity.ok(productTypeService.create(productType));
    }

}
