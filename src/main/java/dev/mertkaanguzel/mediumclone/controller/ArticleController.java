package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.dto.CreateArticleDto;
import dev.mertkaanguzel.mediumclone.dto.UpdateArticleDto;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.repository.UserRepository;
import dev.mertkaanguzel.mediumclone.service.ArticleService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    private final ArticleService articleService;

    public ArticleController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleDto> createArticle(@Valid @RequestBody CreateArticleDto createArticleDto,
                                                    Principal principal) {
        return ResponseEntity.ok(articleService.createArticle(createArticleDto, principal.getName()));
    }

    @GetMapping("/{slug}")
    public ResponseEntity<ArticleDto> getArticleBySlug(@PathVariable String slug, Principal principal) {
        if (principal == null) return ResponseEntity.ok(articleService.findBySlug(slug));

        return ResponseEntity.ok(articleService.findBySlug(slug, principal.getName()));
    }

    @GetMapping
    public ResponseEntity<List<ArticleDto>> getArticles(@RequestParam(value = "tag", required = false) String tag,
                                                        @RequestParam(value = "author", required = false) String author,
                                                        @RequestParam(value = "favorited", required = false) String favoritedBy,
                                                        @RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                                                        @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                        Principal principal) {
        if (principal == null) return ResponseEntity.ok(articleService.findArticles(tag, author,
                favoritedBy, limit, offset));

        return ResponseEntity.ok(articleService.findArticles(tag, author, favoritedBy,
                limit, offset, principal.getName()));
    }

    @GetMapping("/feed")
    public ResponseEntity<List<ArticleDto>> getArticlesFeed(@RequestParam(value = "limit", required = false, defaultValue = "20") Integer limit,
                                                        @RequestParam(value = "offset", required = false, defaultValue = "0") Integer offset,
                                                        Principal principal) {

        return ResponseEntity.ok(articleService.getFeed(limit, offset, principal.getName()));
    }

    @PutMapping("/{slug}")
    public ResponseEntity<ArticleDto> updateArticle(@PathVariable String slug,
                                                    @Valid @RequestBody UpdateArticleDto updateArticleDto,
                                                    Principal principal) {
        Article article = articleService.getArticleBySlug(slug);
        return ResponseEntity.ok(articleService.updateUser(updateArticleDto, article, principal.getName()));
    }

    @DeleteMapping("/{slug}")
    public void deleteArticle(@PathVariable String slug) {
        Article article = articleService.getArticleBySlug(slug);
        articleService.deleteUser(article);
    }
}
