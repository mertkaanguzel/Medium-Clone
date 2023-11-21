package dev.mertkaanguzel.mediumclone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateUserDto(
        @NotBlank(message = "Username must not be empty")
        @Size(min = 8)
        String username,
        @NotBlank(message = "Email must not be empty")
        @Email
        String email,
        @NotBlank(message = "Password must not be empty")
        @Size(min = 8)
        String password) {
}
