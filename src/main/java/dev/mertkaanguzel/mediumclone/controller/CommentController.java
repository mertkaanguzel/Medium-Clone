package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.CommentDto;
import dev.mertkaanguzel.mediumclone.dto.CreateCommentDto;
import dev.mertkaanguzel.mediumclone.model.Comment;
import dev.mertkaanguzel.mediumclone.service.CommentService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/articles/{slug}/comments")
@SecurityRequirement(name = "mediumapi")
public class CommentController {
    private final CommentService commentService;

    public CommentController(CommentService commentService) {
        this.commentService = commentService;
    }

    @PostMapping
    public ResponseEntity<CommentDto> addComment(@PathVariable String slug,
                                                 @Valid @RequestBody CreateCommentDto createCommentDto,
                                                 Principal principal) {
        return ResponseEntity.ok(commentService.createComment(createCommentDto, slug, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<CommentDto>> getComments(@PathVariable String slug, Principal principal) {
        return ResponseEntity.ok(commentService.getComments(slug, principal.getName()));
    }

    @DeleteMapping("/{id}")
    public void deleteComment(@PathVariable String slug, @PathVariable String id) {
        Comment comment = commentService.findComment(slug, id);
        commentService.deleteComment(comment);
    }
}
