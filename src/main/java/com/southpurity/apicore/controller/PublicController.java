package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.AvailableDrums;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.service.CustomerService;
import com.southpurity.apicore.service.PlaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/public")
@RequiredArgsConstructor
public class PublicController {

    private final PlaceService placeService;
    private final CustomerService customerService;

    @GetMapping("/place")
    public ResponseEntity<List<PlaceDocument>> getPlaces() {
        return ResponseEntity.ok(placeService.findAll());
    }

    @GetMapping("/water-drums/{id}/available")
    public ResponseEntity<AvailableDrums> availableWaterDrums(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getAvailableWaterDrums(id));
    }

}
