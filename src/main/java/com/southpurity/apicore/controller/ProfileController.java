package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.profile.ProfileResponse;
import com.southpurity.apicore.model.UserDocument;
import com.southpurity.apicore.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final ProfileService profileService;

    /**
     * Get profile for authenticated user
     *
     * @return actual profile
     */
    @GetMapping
    public ResponseEntity<ProfileResponse> get() {
        return ResponseEntity.ok(profileService.get());
    }


    @PutMapping
    public ResponseEntity<UserDocument> update(@RequestBody ProfileResponse profile) {
        return ResponseEntity.ok(profileService.update(profile));
    }

}
