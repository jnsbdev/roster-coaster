package com.github.jnsbdev.dto.auth;

import java.time.Instant;
import java.util.UUID;

public record RegisterResponse(
        UUID id,
        String name,
        String email,
        Instant timestamp
) {}
