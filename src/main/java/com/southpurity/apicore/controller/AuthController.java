package com.southpurity.apicore.controller;

import com.southpurity.apicore.config.JwtTokenService;
import com.southpurity.apicore.dto.LoginResponse;
import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.service.JwtUserDetailsService;
import com.southpurity.apicore.service.ProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenUtil;
    private final ProfileService profileService;
    private final JwtUserDetailsService userDetailsService;

    @PostMapping("/auth/token")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody UserDTO authenticationRequest) throws Exception {
        final Authentication auth = authenticate(authenticationRequest.getEmail(), authenticationRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(new LoginResponse(token, profileService.get()));
    }

    @PostMapping("/register")
    public ResponseEntity<UserDocument> saveUser(@RequestBody UserDocument user) {
        return ResponseEntity.ok(userDetailsService.create(user));
    }

    private Authentication authenticate(String email, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
