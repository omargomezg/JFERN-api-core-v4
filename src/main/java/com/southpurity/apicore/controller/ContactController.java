package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.ContactRequest;
import com.southpurity.apicore.service.EmailService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/contact")
public class ContactController {

    private final EmailService emailService;

    @PostMapping
    public ResponseEntity<?> sendContact(@RequestBody ContactRequest contactDto) {
        emailService.sendContactEmail(contactDto);
        return ResponseEntity.ok().build();
    }

}
