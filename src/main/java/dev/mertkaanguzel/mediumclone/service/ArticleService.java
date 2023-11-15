package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.exception.ArticleNotFoundException;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import jakarta.annotation.PostConstruct;
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

    public ArticleDto getArticleBySlug(String slug, String username) {
        //UserAccount author = userService.getUserByUserName(username);
        //Article article = articleRepository.getArticleBySlug(slug);
        //article.setUser(author);
        Article article = articleRepository.getArticleWithAuthorBySlug(slug)
                .orElseThrow(() -> new ArticleNotFoundException("Article not found with given slug: " + slug));
        return ArticleDto.fromArticle(article);
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
