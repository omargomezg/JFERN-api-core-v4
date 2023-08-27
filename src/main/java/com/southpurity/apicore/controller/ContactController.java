package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.ContactRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/contact")
public class ContactController {

    @PostMapping
    public ResponseEntity<?> sendContact(@RequestBody ContactRequest contactDto) {
        return ResponseEntity.ok().build();
    }

}
