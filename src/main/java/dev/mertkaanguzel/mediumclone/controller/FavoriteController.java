package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.service.ArticleService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/articles/{slug}/favorite")
public class FavoriteController {
    private final ArticleService articleService;


    public FavoriteController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    public ResponseEntity<ArticleDto> favorite(@PathVariable String slug, Principal principal) {
        return ResponseEntity.ok(articleService.addFavorite(slug, principal.getName()));
    }

    @DeleteMapping
    public void unfavorite(@PathVariable String slug, Principal principal) {
        articleService.removeFavorite(slug, principal.getName());
    }

}
