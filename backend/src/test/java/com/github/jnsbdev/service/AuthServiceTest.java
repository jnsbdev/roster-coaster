package com.github.jnsbdev.service;

import com.github.jnsbdev.dto.auth.LoginRequest;
import com.github.jnsbdev.dto.auth.LoginResponse;
import com.github.jnsbdev.dto.auth.RegisterRequest;
import com.github.jnsbdev.dto.auth.RegisterResponse;
import com.github.jnsbdev.dto.user.CreateUserDto;
import com.github.jnsbdev.dto.user.UserDto;
import com.github.jnsbdev.entity.User;
import com.github.jnsbdev.security.JwtUtil;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    JwtUtil jwtUtil = mock(JwtUtil.class);
    AuthenticationManager authenticationManager = mock(AuthenticationManager.class);
    UserService userService = mock(UserService.class);

    AuthService authService = new AuthService(userService, jwtUtil, authenticationManager);

    @Test
    void register_shouldReturnRegisterResponse_whenSuccessful() {
        // Arrange
        RegisterRequest request = new RegisterRequest(
                "Max Mustermann",
                "max@example.com",
                "secret123"
        );

        UUID expectedId = UUID.fromString("123e4567-e89b-12d3-a456-426614174000");

        UserDto mockUserDto = new UserDto(
                expectedId,
                "Max Mustermann",
                "max@example.com",
                List.of(),
                List.of()
        );

        when(userService.createUser(any(CreateUserDto.class))).thenReturn(mockUserDto);

        // Act
        RegisterResponse response = authService.register(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(expectedId);
        assertThat(response.name()).isEqualTo("Max Mustermann");
        assertThat(response.email()).isEqualTo("max@example.com");
        assertThat(response.timestamp()).isBeforeOrEqualTo(Instant.now());
    }

    @Test
    void register_shouldThrowException_whenUserServiceThrows() {
        // Arrange
        RegisterRequest request = new RegisterRequest("Max", "max@example.com", "password123");

        doThrow(new IllegalArgumentException("Email already in use"))
                .when(userService).createUser(any(CreateUserDto.class));

        // Act + Assert
        assertThatThrownBy(() -> authService.register(request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email already in use");

        verify(userService).createUser(any(CreateUserDto.class));
    }

    @Test
    void login_shouldReturnLoginResponse_whenAuthenticationSucceeds() {
        // Arrange
        LoginRequest request = new LoginRequest("max@example.com", "password123");

        User user = new User(
                UUID.fromString("123e4567-e89b-12d3-a456-426614174000"),
                "Max Mustermann",
                "max@example.com",
                "hashedPassword",
                List.of(),
                List.of()
        );

        UserDto userDto = new UserDto(
                user.id(),
                user.name(),
                user.email(),
                List.of(),
                List.of()
        );

        String expectedToken = "mocked-jwt-token";

        // Mocks
        when(userService.findUserEntityByEmail(request.email())).thenReturn(user);
        when(jwtUtil.generateToken(user)).thenReturn(expectedToken);
        when(userService.findUserDtoByEmail(request.email())).thenReturn(userDto);

        // Act
        LoginResponse response = authService.login(request);

        // Assert
        assertThat(response).isNotNull();
        assertThat(response.token()).isEqualTo(expectedToken);
        assertThat(response.user()).isEqualTo(userDto);

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
    }

    @Test
    void login_shouldThrowException_whenAuthenticationFails() {
        // Arrange
        LoginRequest request = new LoginRequest("max@example.com", "wrongpassword");

        doThrow(new BadCredentialsException("Invalid credentials"))
                .when(authenticationManager)
                .authenticate(any(UsernamePasswordAuthenticationToken.class));

        // Act + Assert
        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BadCredentialsException.class)
                .hasMessageContaining("Invalid credentials");

        verifyNoMoreInteractions(userService, jwtUtil);
    }

}