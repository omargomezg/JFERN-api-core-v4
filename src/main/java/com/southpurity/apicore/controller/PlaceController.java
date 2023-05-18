package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.PlaceDTO;
import com.southpurity.apicore.dto.customer.MyAddressResponse;
import com.southpurity.apicore.service.CustomerService;
import com.southpurity.apicore.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/place")
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceService placeService;
    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<List<PlaceDTO>> getAll() {
        return ResponseEntity.ok(placeService.findAll());
    }

    @GetMapping("/customer")
    public ResponseEntity<List<MyAddressResponse>> getUserPlaces() {
        return ResponseEntity.ok(customerService.getMyPlaces());
    }

}
