package com.github.jnsbdev.dto.auth;

import com.github.jnsbdev.dto.user.UserDto;

public record LoginResponse(UserDto user, String token) {}
