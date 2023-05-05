package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.customer.CustomerPlaceRequest;
import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
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

    @PostMapping("/place")
    public ResponseEntity<?> addPlace(@Valid @RequestBody CustomerPlaceRequest customerPlace) {
        customerService.addPlace(customerPlace);
        return ResponseEntity.ok().build();
    }
}
