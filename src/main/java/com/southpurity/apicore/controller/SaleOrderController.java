package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.SaleOrderFilter;
import com.southpurity.apicore.persistence.model.saleorder.SaleOrderDocument;
import com.southpurity.apicore.service.SaleOrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sale-order")
@RequiredArgsConstructor
public class SaleOrderController {

    private final SaleOrderService saleOrderService;

    @GetMapping
    public ResponseEntity<Page<SaleOrderDocument>> getAll(SaleOrderFilter filter) {
        return ResponseEntity.ok(saleOrderService.getAll(filter));
    }

}
