package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.dto.ProfileDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateArticleDto;
import dev.mertkaanguzel.mediumclone.exception.ArticleAlreadyExistsException;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import org.junit.jupiter.api.*;
import org.springframework.data.domain.Pageable;

import java.time.Clock;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static dev.mertkaanguzel.mediumclone.TestSupport.*;
import static dev.mertkaanguzel.mediumclone.TestSupport.getLocalDateTime;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class ArticleServiceTest {
    private ArticleService articleService;
    private ArticleRepository articleRepository;
    private UserService userService;
    private TagService tagService;

    private final String username = "Jacob";
    private final UserAccount user = new UserAccount("Jacob", "jakejake", "jake@jake.jake",
            "I work at statefarm", "https://api.realworld.io/images/smiley-cyrus.jpg");
    private final Article article = new Article(user, "How to train your dragon?", "Ever wonder how?",
            "It takes a Jacobian", getLocalDateTime(), null, 0);

    private final ProfileDto profileDto = new ProfileDto("Jacob", "I work at statefarm","https://api.realworld.io/images/smiley-cyrus.jpg", false);
    private final ArticleDto articleDto = new ArticleDto("how-to-train-your-dragon?", "How to train your dragon?",
            "Ever wonder how?", "It takes a Jacobian", List.of(), getLocalDateTime(),
            null, false, 0, profileDto);

    @BeforeEach
    void setUp() {
        articleRepository = mock(ArticleRepository.class);
        userService = mock(UserService.class);
        tagService = mock(TagService.class);

        Clock clock = mock(Clock.class);
        when(clock.instant()).thenReturn(getInstant());
        when(clock.getZone()).thenReturn(Clock.systemDefaultZone().getZone());

        articleService = new ArticleService(articleRepository, userService,
                tagService, clock);
    }

    @Test
    void testCreateArticle_whenArticleSlugExists_shouldThrowArticleAlreadyExistsException() {
        CreateArticleDto createArticleDto = new CreateArticleDto("Did you train your dragon?",
                "Ever wonder how?", "It takes a Jacobian", null);

        when(userService.findUserByName(anyString())).thenReturn(user);
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));
        when(articleRepository.saveAndFlush(any(Article.class))).thenAnswer(i -> i.getArguments()[0]);

        assertThrows(ArticleAlreadyExistsException.class, () -> articleService.createArticle(createArticleDto, username));
    }

    @Test
    void testCreateArticle_whenArticleSlugNotExist_shouldCreateArticle() {
        CreateArticleDto createArticleDto = new CreateArticleDto("How to train your dragon?",
                "Ever wonder how?", "It takes a Jacobian", null);

        when(userService.findUserByName(anyString())).thenReturn(user);
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        when(articleRepository.saveAndFlush(any(Article.class))).thenAnswer(i -> i.getArguments()[0]);

        ArticleDto result = articleService.createArticle(createArticleDto, username);
        assertEquals(articleDto, result);

    }

    @Test
    void testFindArticleBySlug_whenArticleSlugNotExists_shouldThrowArticleNotFoundException() {
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class, () -> articleService.findArticleBySlug(article.getSlug()));

    }

    @Test
    void testFindArticleBySlug_whenArticleSlugExists_shouldReturnArticle() {
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));
        Article result = articleService.findArticleBySlug(article.getSlug());
        assertEquals(article, result);
    }

    @Test
    void testGetArticleBySlug_whenArticleSlugNotExists_shouldThrowArticleNotFoundException() {
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.empty());
        assertThrows(ArticleNotFoundException.class, () -> articleService.getArticleBySlug(article.getSlug(), username));
    }

    @Test
    void testGetArticleBySlug_whenArticleSlugExists_shouldReturnArticleDto() {
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));
        ArticleDto result = articleService.getArticleBySlug(article.getSlug(), username);
        assertEquals(articleDto, result);
    }

    @Test
    void testGetArticleBySlug_whenArticleSlugExistsAndMethodCalledWith1Param_shouldReturnArticleDto() {
        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));
        ArticleDto result = articleService.getArticleBySlug(article.getSlug());
        assertEquals(articleDto, result);
    }

    @Test
    void testGetArticleBySlug_whenArticleSlugExistsAndMethodCalledWith2Params_shouldReturnArticleDto() {
        ArticleDto updatedArticleDto = new ArticleDto("how-to-train-your-dragon?", "How to train your dragon?",
                "Ever wonder how?", "It takes a Jacobian", List.of(), getLocalDateTime(),
                null, true, 0, profileDto);
        Article articleWithFavoritedByList = article;
        articleWithFavoritedByList.setFavoritedByList(Set.of(user));

        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(articleWithFavoritedByList));
        ArticleDto result = articleService.getArticleBySlug(article.getSlug(), username);

        assertEquals(updatedArticleDto, result);
    }

    @Test
    void testGetArticlesFeed_shouldReturnListOfArticleDto() {
        List<ArticleDto> articleDtoList = List.of(articleDto);


        when(articleRepository.findFeed(anyString(), any(Pageable.class))).thenReturn(List.of(article));
        List<ArticleDto> result = articleService.getArticlesFeed(1, 0, username);

        assertEquals(articleDtoList, result);
    }

    @Test
    void testupdateArticle_shouldReturnArticleDto() {
        ArticleDto updatedArticleDto = new ArticleDto("did-you-train-your-dragon?", "Did you train your dragon?",
                "Ever wonder how?", "It takes a Jacobian", List.of(), getLocalDateTime(),
                getLocalDateTime(), false, 0, profileDto);
        UpdateArticleDto updateArticleDto = new UpdateArticleDto("Did you train your dragon?",
                null, null);

        when(articleRepository.save(any(Article.class))).thenAnswer(i -> i.getArguments()[0]);
        ArticleDto result = articleService.updateArticle(updateArticleDto, article, username);

        assertEquals(updatedArticleDto, result);
    }

    @Test
    void testAddFavorite_shouldReturnArticleDto() {
        ArticleDto updatedArticleDto = new ArticleDto("how-to-train-your-dragon?", "How to train your dragon?",
                "Ever wonder how?", "It takes a Jacobian", List.of(), getLocalDateTime(),
                getLocalDateTime(), true, 1, profileDto);

        when(articleRepository.findBySlug(anyString())).thenReturn(Optional.of(article));
        when(userService.findUserByName(anyString())).thenReturn(user);
        when(articleRepository.save(any(Article.class))).thenAnswer(i -> i.getArguments()[0]);

        ArticleDto result = articleService.addFavorite(article.getSlug(), username);

        assertEquals(updatedArticleDto, result);
    }

}
