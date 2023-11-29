package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateArticleDto;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Tag;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.stream.Collectors;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;
    private final TagService tagService;

    public ArticleService(ArticleRepository articleRepository, UserService userService, TagService tagService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
        this.tagService = tagService;
    }

    public ArticleDto createArticle(CreateArticleDto createArticleDto, String username) {
        Article article = new Article(
                userService.getUserByUserName(username),
                createArticleDto.title(),
                createArticleDto.description(),
                createArticleDto.body(),
                LocalDateTime.now(),
                null,
                0
        );
        article.setTags(new HashSet<>(tagService.createTags(createArticleDto.tagList())));
        //createArticleDto.tagList().forEach(name -> article.getTags().add(new Tag(name)));

        return ArticleDto.fromArticle(articleRepository.saveAndFlush(article), username);
    }

    public Article getArticleBySlug(String slug) {
        //UserAccount author = userService.getUserByUserName(username);
        //Article article = articleRepository.getArticleBySlug(slug);
        //article.setUser(author);
        return articleRepository.getArticleBySlug(slug)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with given slug: " + slug));
    }

    public ArticleDto findBySlug(String slug, String username) {
        return ArticleDto.fromArticle(getArticleBySlug(slug), username);
    }

    @PreAuthorize("#article.user.username == authentication.name")
    public ArticleDto updateUser(UpdateArticleDto updateArticleDto, Article article, String username) {
        if (updateArticleDto.title() != null) article.setTitle(updateArticleDto.title());
        if (updateArticleDto.description() != null) article.setDescription(updateArticleDto.description());
        if (updateArticleDto.body() != null) article.setBody(updateArticleDto.body());

        return ArticleDto.fromArticle(articleRepository.save(article), username);
    }

    @PreAuthorize("#article.user.username == authentication.name")
    public void deleteUser(Article article) {
        articleRepository.delete(article);
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
