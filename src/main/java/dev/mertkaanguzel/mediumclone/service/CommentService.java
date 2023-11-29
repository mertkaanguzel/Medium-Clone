package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.CommentDto;
import dev.mertkaanguzel.mediumclone.dto.CreateCommentDto;
import dev.mertkaanguzel.mediumclone.exception.CommentNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Comment;
import dev.mertkaanguzel.mediumclone.repository.CommentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final UserService userService;
    private final ArticleService articleService;

    public CommentService(CommentRepository commentRepository, UserService userService, ArticleService articleService) {
        this.commentRepository = commentRepository;
        this.userService = userService;
        this.articleService = articleService;
    }

    public CommentDto addComment(CreateCommentDto createCommentDto, String slug, String username) {
        Comment comment = new Comment(
                userService.getUserByUserName(username),
                articleService.getArticleBySlug(slug),
                createCommentDto.body(),
                LocalDateTime.now(),
                null
        );

        return CommentDto.fromComment(commentRepository.saveAndFlush(comment), username);
    }

    public List<CommentDto> getComments(String slug, String username) {
        return commentRepository.getAllByArticleSlug(slug)
                .stream().map(comment -> CommentDto.fromComment(comment, username)).toList();
    }

    public Comment getComment(String slug, String id) {
        return commentRepository.getByArticleSlugAndId(slug, Long.parseLong(id))
                .orElseThrow(() -> new CommentNotFoundException("Comment not found with given slug"));
    }

    @PreAuthorize("#comment.user.username == authentication.name")
    public void deleteComment(Comment comment) {
        commentRepository.delete(comment);
    }
}
