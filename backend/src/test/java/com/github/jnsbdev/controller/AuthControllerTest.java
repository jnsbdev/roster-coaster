package com.github.jnsbdev.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.jnsbdev.dto.auth.LoginRequest;
import com.github.jnsbdev.dto.auth.LoginResponse;
import com.github.jnsbdev.dto.auth.RegisterRequest;
import com.github.jnsbdev.dto.auth.RegisterResponse;
import com.github.jnsbdev.dto.user.UserDto;
import com.github.jnsbdev.entity.User;
import com.github.jnsbdev.service.AuthService;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthControllerTest {

    @Autowired
    private MockMvc mvc;

    @MockitoBean
    private AuthService authService;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void register_shouldReturn201_whenSuccessful() throws Exception {
        // given
        RegisterRequest request =
                new RegisterRequest("Test User", "test@example.com", "password123");
        RegisterResponse response =
                new RegisterResponse(UUID.randomUUID(),
                        "Test User",
                        "test@example.com",
                        Instant.now());

        // when
        Mockito.when(authService.register(any(RegisterRequest.class)))
                .thenReturn(response);

        // then
        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void register_shouldReturn201_andAuthResponse_whenSuccessful() throws Exception {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password123");
        UUID userId = UUID.randomUUID();
        User user = new User(userId, "Test User", "test@example.com", "hashed", List.of(), List.of());
        RegisterResponse registerResponse = new RegisterResponse(UUID.randomUUID(), user.name(), user.email(), Instant.now());

        Mockito.when(authService.register(any(RegisterRequest.class))).thenReturn(registerResponse);

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Test User"))
                .andExpect(jsonPath("$.email").value("test@example.com"));
    }

    @Test
    void register_shouldReturn400_whenEmailAlreadyExists() throws Exception {
        RegisterRequest request = new RegisterRequest("Test User", "test@example.com", "password123");

        Mockito.when(authService.register(any(RegisterRequest.class)))
                .thenThrow(new IllegalArgumentException("Email already in use"));

        mvc.perform(post("/api/v1/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void login_shouldReturn200_andAuthResponse_whenSuccessful() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "password123");
        UUID userId = UUID.randomUUID();
        UserDto user = new UserDto(userId, "Test User", "test@example.com",  List.of(), List.of());
        LoginResponse authResult = new LoginResponse(user, "jwt-token");

        Mockito.when(authService.login(any(LoginRequest.class))).thenReturn(authResult);

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.token").value("jwt-token"))
                .andExpect(jsonPath("$.user.name").value("Test User"))
                .andExpect(jsonPath("$.user.email").value("test@example.com"));
    }

    @Test
    void login_shouldReturn401_whenBadCredentials() throws Exception {
        LoginRequest request = new LoginRequest("test@example.com", "wrongpassword");

        Mockito.when(authService.login(any(LoginRequest.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        mvc.perform(post("/api/v1/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isUnauthorized());
    }
}