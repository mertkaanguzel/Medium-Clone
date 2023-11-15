package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.UserAccount;

import java.time.LocalDateTime;

public record ArticleDto (String slug,String title, String description, String body,
                         LocalDateTime createdAt, LocalDateTime updatedAt, Integer favoritesCount, ProfileDto profile)
{
    public static ArticleDto fromArticle(Article article) {
        return new ArticleDto(article.getSlug(), article.getTitle(), article.getDescription(), article.getBody(),
                article.getCreatedAt(), article.getUpdatedAt(), article.getFavoritesCount(), ProfileDto.fromUserAccount(article.getUser()));
    }
}
