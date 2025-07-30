package com.github.jnsbdev.dto.user;

import lombok.Builder;

import java.util.List;
import java.util.UUID;

@Builder
public record UserDto(
        UUID id,
        String name,
        String email,
        List<UUID> organizationIds,
        List<UUID> adminOrganizationIds
) {}