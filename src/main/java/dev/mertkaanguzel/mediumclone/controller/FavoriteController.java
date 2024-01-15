package dev.mertkaanguzel.mediumclone.controller;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.service.ArticleService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/articles/{slug}/favorite")
@SecurityRequirement(name = "mediumapi")
public class FavoriteController {
    private final ArticleService articleService;


    public FavoriteController(ArticleService articleService) {
        this.articleService = articleService;
    }

    @PostMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    public ResponseEntity<ArticleDto> favorite(@PathVariable String slug, Principal principal) {
        return ResponseEntity.ok(articleService.addFavorite(slug, principal.getName()));
    }

    @DeleteMapping
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
    })
    public void unfavorite(@PathVariable String slug, Principal principal) {
        articleService.removeFavorite(slug, principal.getName());
    }

}
