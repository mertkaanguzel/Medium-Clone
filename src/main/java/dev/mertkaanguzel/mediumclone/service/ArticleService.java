package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateArticleDto;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class ArticleService {
    private final ArticleRepository articleRepository;
    private final UserService userService;

    public ArticleService(ArticleRepository articleRepository, UserService userService) {
        this.articleRepository = articleRepository;
        this.userService = userService;
    }

    public ArticleDto createArticle(CreateArticleDto createArticleDto, String username) {
        Article article = new Article(
                userService.getUserByUserName(username),
                createArticleDto.title().toLowerCase().replace(" ", "-"),
                createArticleDto.title(),
                createArticleDto.description(),
                createArticleDto.body(),
                LocalDateTime.now(),
                null,
                0
        );

        return ArticleDto.fromArticle(articleRepository.saveAndFlush(article));
    }

    public Article getArticleBySlug(String slug) {
        //UserAccount author = userService.getUserByUserName(username);
        //Article article = articleRepository.getArticleBySlug(slug);
        //article.setUser(author);
        return articleRepository.getArticleWithAuthorBySlug(slug)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with given slug: " + slug));
    }

    public ArticleDto findBySlug(String slug) {
        return ArticleDto.fromArticle(getArticleBySlug(slug));
    }

    @PreAuthorize("#article.user.username == authentication.name")
    public ArticleDto updateUser(UpdateArticleDto updateArticleDto, Article article) {
        if (updateArticleDto.title() != null) {
            article.setTitle(updateArticleDto.title());
            article.setSlug(updateArticleDto.title().toLowerCase().replace(" ", "-"));

        }
        if (updateArticleDto.description() != null) article.setDescription(updateArticleDto.description());
        if (updateArticleDto.body() != null) article.setBody(updateArticleDto.body());

        return ArticleDto.fromArticle(articleRepository.save(article));
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
