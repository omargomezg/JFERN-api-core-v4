package com.southpurity.apicore.controller;

import com.southpurity.apicore.persistence.model.ConfigurationDocument;
import com.southpurity.apicore.service.ConfigurationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/configuration")
@RequiredArgsConstructor
public class ConfigurationController {

    private final ConfigurationService configurationService;

    @GetMapping
    public ResponseEntity<ConfigurationDocument> getConfiguration() {
        return ResponseEntity.ok(configurationService.findBySiteName());
    }

    @PutMapping
    public ResponseEntity<ConfigurationDocument> updateConfiguration(@RequestBody ConfigurationDocument configuration) {
        return ResponseEntity.ok(configurationService.update(configuration));
    }
}
