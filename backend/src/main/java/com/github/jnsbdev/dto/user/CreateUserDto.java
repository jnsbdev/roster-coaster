package com.github.jnsbdev.dto.user;

public record CreateUserDto(
        String name,
        String email,
        String password
) implements UserCredentials {}
