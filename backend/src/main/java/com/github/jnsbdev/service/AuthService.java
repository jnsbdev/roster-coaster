package com.github.jnsbdev.service;

import com.github.jnsbdev.dto.auth.LoginResponse;
import com.github.jnsbdev.dto.auth.LoginRequest;
import com.github.jnsbdev.dto.auth.RegisterRequest;
import com.github.jnsbdev.dto.auth.RegisterResponse;
import com.github.jnsbdev.dto.user.CreateUserDto;
import com.github.jnsbdev.dto.user.UserDto;
import com.github.jnsbdev.entity.User;
import com.github.jnsbdev.security.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.time.Instant;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    public RegisterResponse register(RegisterRequest request) {
        CreateUserDto dto = new CreateUserDto(
                request.name(),
                request.email(),
                request.password()
        );
        UserDto created = userService.createUser(dto);
        return new RegisterResponse(
                created.id(),
                created.name(),
                created.email(),
                Instant.now()
        );
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );
        User userEntity = userService.findUserEntityByEmail(request.email());
        String token = jwtUtil.generateToken(userEntity);
        UserDto userDto = userService.findUserDtoByEmail(request.email());
        return new LoginResponse(userDto, token);
    }
}
