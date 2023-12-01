package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDto (String slug,String title, String description, String body,
                          List<String> tagList, LocalDateTime createdAt, LocalDateTime updatedAt,
                          Boolean favorited, Integer favoritesCount, ProfileDto author)
{
    public static ArticleDto fromArticle(Article article, String username) {
        List<String> tagList = article.getTags().stream().map(Tag::getName).toList();
        Boolean favorited = article.getFavoritedByList().stream().anyMatch(user -> user.getUsername().equals(username));

        return new ArticleDto(article.getSlug(), article.getTitle(), article.getDescription(),
                article.getBody(), tagList, article.getCreatedAt(), article.getUpdatedAt(), favorited,
                article.getFavoritesCount(), ProfileDto.fromUserAccount(article.getUser(), username));
    }

    public static ArticleDto fromArticle(Article article) {
        List<String> tagList = article.getTags().stream().map(Tag::getName).toList();

        return new ArticleDto(article.getSlug(), article.getTitle(), article.getDescription(),
                article.getBody(), tagList, article.getCreatedAt(), article.getUpdatedAt(), false,
                article.getFavoritesCount(), ProfileDto.fromUserAccount(article.getUser()));
    }
}


