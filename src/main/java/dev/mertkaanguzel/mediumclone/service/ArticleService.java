package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.config.OffsetBasedPageRequest;
import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateArticleDto;
import dev.mertkaanguzel.mediumclone.exception.ArticleAlreadyExistsException;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TagService tagService;
    private final Clock clock;

    public ArticleService(ArticleRepository articleRepository, UserService userService, TagService tagService, Clock clock) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.tagService = tagService;
        this.clock = clock;
    }

    public ArticleDto createArticle(CreateArticleDto createArticleDto, String username) {
        Article article = new Article(
                userService.findUserByName(username),
                createArticleDto.title(),
                createArticleDto.description(),
                createArticleDto.body(),
                getLocalDateTimeNow(),
                null,
                0
        );
        article.setTags(new HashSet<>(tagService.createTags(createArticleDto.tagList())));
        //createArticleDto.tagList().forEach(name -> article.getTags().add(new Tag(name)));

        if (articleRepository.findBySlug(article.getSlug()).isPresent()) {
            throw new ArticleAlreadyExistsException("Article already exists with given slug: " + article.getSlug());
        }

        return ArticleDto.fromArticle(articleRepository.saveAndFlush(article), username);
    }

    public Article findArticleBySlug(String slug) {
        //UserAccount author = userService.getUserByUserName(username);
        //Article article = articleRepository.getArticleBySlug(slug);
        //article.setUser(author);
        return articleRepository.findBySlug(slug)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with given slug: " + slug));
    }

    public ArticleDto getArticleBySlug(String slug, String username) {
        return ArticleDto.fromArticle(findArticleBySlug(slug), username);
    }

    public ArticleDto getArticleBySlug(String slug) {
        return ArticleDto.fromArticle(findArticleBySlug(slug));
    }

    public List<ArticleDto> getArticles(String tag, String author,
                                        String favoritedBy, int limit,
                                        int offset, String username) {
        Pageable pageable = PageRequest.of(offset, limit);

        return articleRepository.findAllByParams(tag, author, favoritedBy, pageable)
                .stream().map(article -> ArticleDto.fromArticle(article, username)).toList();
    }

    public List<ArticleDto> getArticles(String tag, String author,
                                        String favoritedBy, int limit,
                                        int offset) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit);

        return articleRepository.findAllByParams(tag, author, favoritedBy, pageable)
                .stream().map(ArticleDto::fromArticle).toList();
    }

    public List<ArticleDto> getArticlesFeed(int limit, int offset, String username) {
        Pageable pageable = new OffsetBasedPageRequest(offset, limit);

        return articleRepository.findFeed(username, pageable)
                .stream().map(article -> ArticleDto.fromArticle(article, username)).toList();
    }

    @PreAuthorize("#article.user.username == authentication.name")
    public ArticleDto updateUser(UpdateArticleDto updateArticleDto, Article article, String username) {
        if (updateArticleDto.title() != null) article.setTitle(updateArticleDto.title());
        if (updateArticleDto.description() != null) article.setDescription(updateArticleDto.description());
        if (updateArticleDto.body() != null) article.setBody(updateArticleDto.body());
        article.setUpdatedAt(getLocalDateTimeNow());

        return ArticleDto.fromArticle(articleRepository.save(article), username);
    }

    @PreAuthorize("#article.user.username == authentication.name")
    public void deleteUser(Article article) {
        articleRepository.delete(article);
    }

    public ArticleDto addFavorite(String slug, String username) {
        Article article = findArticleBySlug(slug);

        article.getFavoritedByList().add(userService.findUserByName(username));
        article.setFavoritesCount(article.getFavoritesCount() + 1);

        return ArticleDto.fromArticle(articleRepository.save(article), username);
    }

    public void deleteFavorite(String slug, String username) {
        Article article = findArticleBySlug(slug);

        boolean isIncluded = article.getFavoritedByList().remove(userService.findUserByName(username));
        if (isIncluded) article.setFavoritesCount(article.getFavoritesCount() - 1);

        articleRepository.save(article);
    }

    private LocalDateTime getLocalDateTimeNow() {
        Instant instant = clock.instant();
        return LocalDateTime.ofInstant(
                instant,
                Clock.systemDefaultZone().getZone()
        );
    }
/*
    @PostConstruct
    void initDatabase() {
        UserAccount user = userService.getUserByUserName("john");
        articleRepository.save(
                new Article(user,
                "how-to-train-your-dragon",
                "How to train your dragon",
                "Ever wonder how?",
                "It takes a Jacobian",
                LocalDateTime.now(),
                null,
                0));

    }
    */

}
