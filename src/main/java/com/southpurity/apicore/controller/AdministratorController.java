package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.administrator.PlaceResponseDTO;
import com.southpurity.apicore.model.PlaceDocument;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.model.constant.RoleEnum;
import com.southpurity.apicore.service.AdministratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping("/place")
    public ResponseEntity<PlaceResponseDTO> create(@RequestBody PlaceDocument place) {
        return ResponseEntity.ok(administratorService.savePlace(place));
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDocument>> getUser(@RequestParam RoleEnum role) {
        return ResponseEntity.ok(administratorService.getAllUsers(role));
    }

}
