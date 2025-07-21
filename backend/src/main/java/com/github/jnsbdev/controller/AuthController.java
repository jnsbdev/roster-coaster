package com.github.jnsbdev.controller;

import com.github.jnsbdev.dto.AuthResponse;
import com.github.jnsbdev.dto.AuthResult;
import com.github.jnsbdev.dto.LoginRequest;
import com.github.jnsbdev.dto.RegisterRequest;
import com.github.jnsbdev.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody @Valid RegisterRequest request) {
        try {
            AuthResult result = authService.registerUserAndGenerateToken(request);
            AuthResponse response = new AuthResponse(result.token(), result.user().name(), result.user().email());
            return ResponseEntity.status(201).body(response);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody @Valid LoginRequest request) {
        try {
            AuthResult result = authService.authenticateUserAndGenerateToken(request);
            AuthResponse response = new AuthResponse(result.token(), result.user().name(), result.user().email());
            return ResponseEntity.ok(response);
        } catch (BadCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
    }
}
