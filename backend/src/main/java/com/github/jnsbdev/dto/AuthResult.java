package com.github.jnsbdev.dto;

import com.github.jnsbdev.entity.User;

public record AuthResult(User user, String token) {}
