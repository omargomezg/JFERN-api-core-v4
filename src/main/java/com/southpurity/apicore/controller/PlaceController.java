package com.southpurity.apicore.controller;

import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.repository.PlaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PlaceController {

    private final PlaceRepository placeRepository;

    @GetMapping("/place")
    public ResponseEntity<List<PlaceDocument>> getAll() {
        return ResponseEntity.ok(placeRepository.findAll());
    }
}
