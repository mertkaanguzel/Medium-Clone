package dev.mertkaanguzel.mediumclone.dto;

import jakarta.validation.constraints.NotBlank;

public record UpdateUserDto(
        String email,
        String username,
        String password,
        String image,
        String bio
) {}
