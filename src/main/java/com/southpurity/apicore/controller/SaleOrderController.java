package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.SaleOrderRequest;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale-order")
@RequiredArgsConstructor
public class SaleOrderController {

    private final SaleOrderService saleOrderService;

    @GetMapping
    public ResponseEntity<Page<SaleOrderDocument>> getAll(SaleOrderRequest saleOrderRequest) {
        return ResponseEntity.ok(saleOrderService.getAll(saleOrderRequest));
    }

}
