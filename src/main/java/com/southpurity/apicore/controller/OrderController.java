package com.southpurity.apicore.controller;

import com.southpurity.apicore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/order")
@RequiredArgsConstructor
public class OrderController {

    private final ProductService productService;

    @GetMapping
    public ResponseEntity<?> getOrders(@RequestParam String place, Pageable pageable) {
        return ResponseEntity.ok(productService.getAllAvailable(place, pageable));
    }
}
