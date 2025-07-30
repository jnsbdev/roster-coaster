package com.github.jnsbdev.dto.auth;

import com.github.jnsbdev.dto.user.UserCredentials;

public record RegisterRequest(
        String name,
        String email,
        String password
) implements UserCredentials {}

