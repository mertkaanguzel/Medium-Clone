package dev.mertkaanguzel.mediumclone.dto;

import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Tag;

import java.time.LocalDateTime;
import java.util.List;

public record ArticleDto (String slug,String title, String description, String body, List<String> tagList,
                         LocalDateTime createdAt, LocalDateTime updatedAt, Integer favoritesCount, ProfileDto profile)
{
    public static ArticleDto fromArticle(Article article, String username) {
        List<String> tagList = article.getTags().stream().map(Tag::getName).toList();

        return new ArticleDto(article.getSlug(), article.getTitle(), article.getDescription(), article.getBody(), tagList,
                article.getCreatedAt(), article.getUpdatedAt(), article.getFavoritesCount(), ProfileDto.fromUserAccount(article.getUser(), username));
    }
}
