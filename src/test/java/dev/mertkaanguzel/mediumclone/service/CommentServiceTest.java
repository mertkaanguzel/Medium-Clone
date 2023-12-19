package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.*;
import dev.mertkaanguzel.mediumclone.exception.ArticleAlreadyExistsException;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.exception.CommentNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Comment;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.CommentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Clock;
import java.util.List;
import java.util.Optional;

import static dev.mertkaanguzel.mediumclone.TestSupport.getInstant;
import static dev.mertkaanguzel.mediumclone.TestSupport.getLocalDateTime;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CommentServiceTest {
    private CommentService commentService;
    private CommentRepository commentRepository;
    private UserService userService;
    private ArticleService articleService;

    private final String slug = "how-to-train-your-dragon?";
    private final String username = "Jacob";
    private final UserAccount user = new UserAccount("Jacob", "jakejake", "jake@jake.jake",
            "I work at statefarm", "https://api.realworld.io/images/smiley-cyrus.jpg");
    private final Article article = new Article(user, "How to train your dragon?", "Ever wonder how?",
            "It takes a Jacobian", getLocalDateTime(), null, 0);

    private final CreateCommentDto createCommentDto = new CreateCommentDto("It takes a Jacobian");
    private final ProfileDto profileDto = new ProfileDto("Jacob", "I work at statefarm","https://api.realworld.io/images/smiley-cyrus.jpg", false);
    private final CommentDto commentDto = new CommentDto(null, getLocalDateTime(), null, "It takes a Jacobian", profileDto);
    private final Comment comment = new Comment(user, article, "It takes a Jacobian", getLocalDateTime(), null);

    @BeforeEach
    void setUp() {
        commentRepository = mock(CommentRepository.class);
        userService = mock(UserService.class);
        articleService = mock(ArticleService.class);


        Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(getInstant());
        when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());

        commentService = new CommentService(commentRepository, userService, articleService, clock);
    }

    @Test
    void testCreateComment_shouldCreateComment() {
        when(userService.findUserByName(anyString())).thenReturn(user);
        when(articleService.findArticleBySlug(anyString())).thenReturn(article);
        when(commentRepository.saveAndFlush(any(Comment.class))).thenAnswer(i -> i.getArguments()[0]);

        CommentDto result = commentService.createComment(createCommentDto, slug, username);
        assertEquals(commentDto, result);
    }

    @Test
    void testGetComments_shouldReturnListOfCommentDto() {
        when(commentRepository.findAllByArticleSlug(anyString())).thenReturn(List.of(comment));
        List<CommentDto> result = commentService.getComments(slug, username);
        assertEquals(List.of(commentDto), result);
    }

    @Test
    void testFindComment_whenArticleExists_shouldReturnCommentDto() {
        when(commentRepository.findByArticleSlugAndId(anyString(), any(Long.class))).thenReturn(Optional.of(comment));
        Comment result = commentService.findComment(slug, "1");
        assertEquals(comment, result);
    }

    @Test
    void testFindComment_whenArticleNotExist_shouldThrowCommentNotFoundException() {
        when(commentRepository.findByArticleSlugAndId(anyString(), any(Long.class))).thenReturn(Optional.empty());
        assertThrows(CommentNotFoundException.class, () -> commentService.findComment(slug, "1"));
    }
}
