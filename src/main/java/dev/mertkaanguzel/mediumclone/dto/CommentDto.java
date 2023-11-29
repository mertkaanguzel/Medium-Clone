package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.Comment;

import java.time.LocalDateTime;

public record CommentDto(Long id, LocalDateTime createdAt, LocalDateTime updatedAt, String body, ProfileDto author) {
    public static CommentDto fromComment(Comment comment, String username) {
        return new CommentDto(
                comment.getId(),
                comment.getCreatedAt(),
                comment.getUpdatedAt(),
                comment.getBody(),
                ProfileDto.fromUserAccount(comment.getUser(), username)
        );
    }
}
