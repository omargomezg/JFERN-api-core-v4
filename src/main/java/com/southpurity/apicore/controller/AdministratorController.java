package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.administrator.ConfigurationDTO;
import com.southpurity.apicore.dto.administrator.OrderDTO;
import com.southpurity.apicore.dto.administrator.PlaceResponseDTO;
import com.southpurity.apicore.dto.administrator.UserDTO;
import com.southpurity.apicore.persistence.model.OrderDocument;
import com.southpurity.apicore.persistence.model.PlaceDocument;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.service.AdministratorService;
import com.southpurity.apicore.service.ConfigurationService;
import com.southpurity.apicore.service.JwtUserDetailsService;
import com.southpurity.apicore.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/administrator")
@RequiredArgsConstructor
public class AdministratorController {

    private final AdministratorService administratorService;
    private final ConfigurationService configurationService;
    private final OrderService orderService;
    private final JwtUserDetailsService userDetailsService;

    @GetMapping("/place")
    public ResponseEntity<List<PlaceResponseDTO>> getPlaces() {
        return ResponseEntity.ok(administratorService.getAllPlaces());
    }

    @PostMapping("/place")
    public ResponseEntity<PlaceResponseDTO> create(@RequestBody PlaceDocument place) {
        return ResponseEntity.ok(administratorService.savePlace(place));
    }

    @GetMapping("/user")
    public ResponseEntity<List<UserDTO>> getUser(@RequestParam RoleEnum role) {
        return ResponseEntity.ok(administratorService.getAllUsers(role));
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

    @GetMapping("/configuration")
    public ResponseEntity<ConfigurationDTO> getConfiguration() {
        return ResponseEntity.ok(configurationService.get());
    }

    @PutMapping("/configuration")
    public ResponseEntity<?> updateConfiguration(@RequestBody @Valid ConfigurationDTO configuration) {
        configurationService.update(configuration);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/order")
    public ResponseEntity<OrderDocument> createOrder(@RequestBody OrderDTO order) {
        return ResponseEntity.ok(orderService.create(order));
    }

}
