package com.github.jnsbdev.dto.shift;

import com.github.jnsbdev.entity.ShiftSignup;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

import java.util.List;

@Builder
public record ShiftCreateDTO(
        @NotNull(message = "Duration is mandatory")
        @Valid
        ShiftDurationDTO duration,

        @NotNull(message = "Participants must not be null")
        List<@Valid ShiftSignup> signups,

        int minParticipants,
        int maxParticipants
) {}
