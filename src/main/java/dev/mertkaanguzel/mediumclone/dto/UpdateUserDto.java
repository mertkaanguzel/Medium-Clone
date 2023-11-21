package dev.mertkaanguzel.mediumclone.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;

public record UpdateUserDto(
        @Email
        String email,
        @Size(min = 8)
        String username,
        @Size(min = 8)
        String password,
        String image,
        String bio
) {}
