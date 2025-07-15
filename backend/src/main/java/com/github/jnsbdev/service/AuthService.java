package com.github.jnsbdev.service;

import com.github.jnsbdev.dto.AuthResult;
import com.github.jnsbdev.dto.LoginRequest;
import com.github.jnsbdev.dto.RegisterRequest;
import com.github.jnsbdev.entity.User;
import com.github.jnsbdev.repository.UserRepository;
import com.github.jnsbdev.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IdService idService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public User registerUser(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new IllegalArgumentException("Email already in use");
        }
        String hashedPassword = passwordEncoder.encode(request.password());
        User user = new User(
                idService.randomId(),
                request.name(),
                request.email(),
                hashedPassword,
                List.of(),
                List.of()
        );
        return userRepository.save(user);
    }

    public AuthResult registerUserAndGenerateToken(RegisterRequest request) {
        User user = registerUser(request);
        String token = jwtUtil.generateToken(user);
        return new AuthResult(user, token);
    }

    public AuthResult authenticateUserAndGenerateToken(LoginRequest request) {
        // 1. Authenticate credentials
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.email(), request.password())
        );

        // 2. Load user from DB
        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        // 3. Generate JWT token
        String token = jwtUtil.generateToken(user);

        // 4. Return AuthResult
        return new AuthResult(user, token);
    }
}
