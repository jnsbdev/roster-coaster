package com.github.jnsbdev.entity;

import lombok.With;

import java.time.Instant;

public record ShiftDuration(
        @With
        Instant start,
        @With
        Instant end
) {
}
