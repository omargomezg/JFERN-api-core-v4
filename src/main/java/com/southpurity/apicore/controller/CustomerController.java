package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.customer.CustomerPlaceRequest;
import com.southpurity.apicore.dto.customer.MyOrderResponseDTO;
import com.southpurity.apicore.service.ConfigurationService;
import com.southpurity.apicore.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.bson.types.Decimal128;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final ConfigurationService configurationService;

    @GetMapping("/order")
    public ResponseEntity<List<MyOrderResponseDTO>> getMyOrders() {
        return ResponseEntity.ok(customerService.getMyOrders());
    }

    @PostMapping("/place")
    public ResponseEntity<?> addPlace(@Valid @RequestBody CustomerPlaceRequest customerPlace) {
        customerService.addPlace(customerPlace);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/water-drums/{id}/available")
    public ResponseEntity<Integer> availableWaterDrums(@PathVariable String id) {
        return ResponseEntity.ok(customerService.getAvailableWaterDrums(id));
    }

    @GetMapping("/water-drums/price")
    public ResponseEntity<Decimal128> priceWaterDrums() {
        var configuration = configurationService.get();
        return ResponseEntity.ok(configuration.getPrice());
    }

}
