package com.github.jnsbdev.exception;

import java.time.LocalDateTime;

public record ErrorMessage(
        String errorMessage,
        LocalDateTime timestamp
) {
    public ErrorMessage(String errorMessage) {
        this(errorMessage, LocalDateTime.now());
    }

    public ErrorMessage {
        timestamp = LocalDateTime.now();
    }
}
