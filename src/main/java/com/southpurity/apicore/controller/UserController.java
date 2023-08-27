package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.dto.UserFilter;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ConversionService conversionService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserDTO user) {
        userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        var user = userService.findById(id);
        return ResponseEntity.ok(conversionService.convert(user, UserDTO.class));
    }

    @GetMapping
    public ResponseEntity<Page<UserDocument>> getAll(UserFilter filter,
                                                     Pageable pageable) {
        var pageOfUsers = userService.findAllUsers(pageable, filter);
        return ResponseEntity.ok(pageOfUsers);
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(@PathVariable String id) {
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserDTO> updatePassword(@PathVariable String id, @RequestBody UserDTO user) {
        user.setId(id);
        return ResponseEntity.ok(userService.updatePassword(user));
    }

}
