package dev.mertkaanguzel.mediumclone.dto;

import jakarta.validation.constraints.NotBlank;

import java.util.List;

public record CreateArticleDto(
        @NotBlank(message = "Title must not be empty")
        String title,
        @NotBlank(message = "Description must not be empty")
        String description,
        @NotBlank(message = "Body must not be empty")
        String body,
        List<String> tagList
) {}
