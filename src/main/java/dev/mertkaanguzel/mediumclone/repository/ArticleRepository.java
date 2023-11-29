package dev.mertkaanguzel.mediumclone.repository;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.model.Article;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a JOIN FETCH a.user WHERE a.slug = (:slug)") // try using it after creating the ArticleDto
    Optional<Article> getArticleWithAuthorBySlug(@Param("slug") String slug);

    Optional<Article> getArticleBySlug(String slug);

}
