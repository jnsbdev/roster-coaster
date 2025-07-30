package com.github.jnsbdev.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public interface UserCredentials {
    @NotBlank
    @Size(max = 100)
    String name();

    @NotBlank
    @Email
    String email();

    @NotBlank
    @Size(min = 8, max = 64)
    String password();
}
