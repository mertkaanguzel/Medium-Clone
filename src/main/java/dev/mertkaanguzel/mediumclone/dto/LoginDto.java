package dev.mertkaanguzel.mediumclone.dto;

import jakarta.validation.constraints.NotBlank;

public record LoginDto(
        @NotBlank(message = "Email must not be empty")
        String email,
        @NotBlank(message = "Password must not be empty")
        String password
) {}
