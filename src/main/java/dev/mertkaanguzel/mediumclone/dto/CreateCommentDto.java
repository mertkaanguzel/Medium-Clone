package dev.mertkaanguzel.mediumclone.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCommentDto(
        @NotBlank(message = "Body must not be empty")
        String body
) {}
