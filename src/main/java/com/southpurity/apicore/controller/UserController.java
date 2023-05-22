package com.southpurity.apicore.controller;

import com.southpurity.apicore.dto.PageDTO;
import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;
import com.southpurity.apicore.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<Void> create(@RequestBody UserDTO user) {
        userService.create(user);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDTO> getById(@PathVariable String id) {
        return ResponseEntity.ok(userService.findById(id));
    }

    @GetMapping
    public ResponseEntity<PageDTO<UserDTO>> getAll(@RequestParam(required = false) RoleEnum role, Pageable pageable) {
        var users = role != null ? userService.findByRole(role, pageable) : userService.findAllUsers(pageable);
        return ResponseEntity.ok(new PageDTO<>(users.getContent(), users.getPageable(), users.getTotalElements()));
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
