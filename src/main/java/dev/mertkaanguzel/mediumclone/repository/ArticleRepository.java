package dev.mertkaanguzel.mediumclone.repository;

import dev.mertkaanguzel.mediumclone.model.Article;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    Article getArticleBySlug(String slug);
}
