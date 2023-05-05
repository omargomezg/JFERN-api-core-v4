package com.southpurity.apicore.controller;

import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.repository.PlaceRepository;
import com.southpurity.apicore.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepository placeRepository;
    private final CustomerService customerService;

    @GetMapping("/place")
    public ResponseEntity<List<PlaceDocument>> getAll() {
        return ResponseEntity.ok(placeRepository.findAll());
    }

    @GetMapping("/place/customer")
    public ResponseEntity<List<PlaceDocument>> getUserPlaces() {
        return ResponseEntity.ok(customerService.getMyPlaces());
    }

}
