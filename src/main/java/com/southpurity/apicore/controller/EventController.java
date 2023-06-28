package com.southpurity.apicore.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/event")
@RequiredArgsConstructor
public class EventController {

    @GetMapping
    public ResponseEntity<?> get() {
        return ResponseEntity.ok().build();
    }

    @PutMapping
    public ResponseEntity<?> update() {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<?> create() {
        return ResponseEntity.ok().build();
    }

}
