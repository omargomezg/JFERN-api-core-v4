package com.southpurity.apicore.controller;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.southpurity.apicore.config.JwtTokenService;
import com.southpurity.apicore.dto.LoginResponse;
import com.southpurity.apicore.dto.TokenDTO;
import com.southpurity.apicore.dto.UserDTO;
import com.southpurity.apicore.persistence.model.UserDocument;
import com.southpurity.apicore.service.EmailService;
import com.southpurity.apicore.service.JwtUserDetailsService;
import com.southpurity.apicore.service.ProfileService;
import com.southpurity.apicore.service.UserService;
import com.southpurity.apicore.utils.Utils;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.NoSuchElementException;
import com.southpurity.apicore.persistence.model.constant.RoleEnum;

@RestController
@RequiredArgsConstructor
public class AuthController {

    @Value("${google.client-id}")
    private String clientId;

    private final AuthenticationManager authenticationManager;
    private final JwtTokenService jwtTokenUtil;
    private final ProfileService profileService;
    private final UserService userService;
    private final JwtUserDetailsService userDetailsService;
    private final EmailService emailService;

    @PostMapping("/auth/token")
    public ResponseEntity<LoginResponse> createAuthenticationToken(@RequestBody UserDTO authenticationRequest)
            throws Exception {
        final Authentication auth = authenticate(authenticationRequest.getEmail().toLowerCase(),
                authenticationRequest.getPassword());
        SecurityContextHolder.getContext().setAuthentication(auth);
        String token = jwtTokenUtil.generateToken(auth);
        return ResponseEntity.ok(new LoginResponse(token, profileService.get()));
    }

    @PostMapping("/auth/restore")
    public ResponseEntity<UserDocument> restorePassword(@RequestBody UserDocument user) {
        var userDocument = userDetailsService.getByEmail(user.getEmail());
        var code = Utils.generateCode(6).toUpperCase();
        emailService.sendRestorePasswordEmail(userDocument, code);
        return ResponseEntity.ok(userService.updateCodeForPwdRecovery(userDocument, code));
    }

    @PostMapping("/auth/restore/{code}")
    public ResponseEntity<UserDocument> restorePassword(@RequestBody UserDocument user, @PathVariable String code) {
        user.getPasswordReset().setCode(code);
        var result = userService.updatePwdWithCode(user);
        // send email
        return ResponseEntity.ok(result);
    }

    @PostMapping("/auth/google")
    public ResponseEntity<?> googleAuth(@RequestBody TokenDTO token) throws GeneralSecurityException, IOException {
        var transport = new NetHttpTransport();
        var factory = GsonFactory.getDefaultInstance();
        var verifier = new GoogleIdTokenVerifier.Builder(transport, factory)
                .setAudience(Collections.singletonList(clientId))
                .build();
        var idToken = verifier.verify(token.getToken());
        if (idToken != null) {
            GoogleIdToken.Payload payload = idToken.getPayload();

            // Extraemos la información del usuario
            String email = payload.getEmail();
            String name = (String) payload.get("name");

            // Lógica de Negocio:
            UserDocument user;
            try {
                user = userDetailsService.getByEmail(email);
            } catch (NoSuchElementException e) {
                user = UserDocument.builder()
                        .email(email)
                        .fullName(name)
                        .role(RoleEnum.CUSTOMER)
                        .password(Utils.generateCode(10))
                        .build();
                user = userDetailsService.create(user);
            }

            Authentication auth = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(auth);
            String jwtToken = jwtTokenUtil.generateToken(auth);

            return ResponseEntity.ok(new LoginResponse(jwtToken, profileService.get()));

        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Token de Google inválido");
        }
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
