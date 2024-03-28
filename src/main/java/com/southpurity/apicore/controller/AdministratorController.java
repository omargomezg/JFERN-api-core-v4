package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.PlaceDTO;
import com.southpurity.apicore.dto.ProductDTO;
import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.ProductDocument;
import com.southpurity.apicore.service.AdministratorService;
import com.southpurity.apicore.service.ConfigurationService;
import com.southpurity.apicore.service.JwtUserDetailsService;
import com.southpurity.apicore.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;
    private final ConfigurationService configurationService;
    private final ProductService productService;
    private final JwtUserDetailsService userDetailsService;

    @PostMapping("/place")
    public ResponseEntity<PlaceDTO> createPlace(@RequestBody PlaceDocument place) {
        return ResponseEntity.ok(administratorService.savePlace(place));
    }

    @GetMapping("/user/{id}")
    public ResponseEntity<UserDTO> getClientById(@PathVariable String id) {
        return ResponseEntity.ok(userDetailsService.get(id));
    }

    @PutMapping("/user/{id}")
    public ResponseEntity<Void> updateClient(@PathVariable String id,
                                             @RequestBody UserDTO user) {
        user.setId(id);
        userDetailsService.update(user);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/product")
    public ResponseEntity<ProductDocument> createOrder(@Valid @RequestBody ProductDTO order) {
        return ResponseEntity.ok(productService.create(order));
    }

}
