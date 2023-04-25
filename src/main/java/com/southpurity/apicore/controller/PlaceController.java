package com.southpurity.apicore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;

@RestController
@CrossOrigin
@RequiredArgsConstructor
public class PlaceController {
    @GetMapping("/place")
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(new ArrayList<>());
    }
}
