package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.administrator.PlaceResponseDTO;
import com.southpurity.apicore.service.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;

    @GetMapping("/place")
    public ResponseEntity<List<PlaceResponseDTO>> getPlaces() {
        return ResponseEntity.ok(administratorService.getAllPlaces());
    }

}
