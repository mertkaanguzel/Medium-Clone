package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import dev.mertkaanguzel.mediumclone.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService, UserRepository userRepository) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<Article> createArticle(@RequestBody CreateArticleDto createArticleDto, Principal principal) {
        return ResponseEntity.ok(articleService.createArticle(createArticleDto, principal.getName()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<Article> getArticleBySlug(@PathVariable String slug, Principal principal) {
        return ResponseEntity.ok(articleService.getArticleBySlug(slug, principal.getName()));
    }
}
