package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping("/order")
    public ResponseEntity<List<MyOrderResponseDTO>> getMyOrders() {
        return ResponseEntity.ok(customerService.getMyOrders());
    }
}
