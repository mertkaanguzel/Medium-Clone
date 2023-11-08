package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import dev.mertkaanguzel.mediumclone.repository.ArticleRepository;
import jakarta.annotation.PostConstruct;
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

    public Article createArticle(CreateArticleDto createArticleDto, String username) {
        Article article = new Article(
                userService.getUserByUserName(username),
                createArticleDto.slug(),
                createArticleDto.title(),
                createArticleDto.description(),
                createArticleDto.body(),
                LocalDateTime.now(),
                null,
                0
        );

        return articleRepository.saveAndFlush(article);
    }

    public Article getArticleBySlug(String slug) {
        return articleRepository.getArticleBySlug(slug);
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
