package dev.mertkaanguzel.mediumclone.repository;

import dev.mertkaanguzel.mediumclone.dto.ArticleDto;
import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Tag;
import dev.mertkaanguzel.mediumclone.model.UserAccount;
import jakarta.persistence.TypedQuery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ArticleRepository extends JpaRepository<Article, Long> {
    @Query("SELECT a FROM Article a JOIN FETCH a.user WHERE a.slug = (:slug)") // try using it after creating the ArticleDto
    Optional<Article> getArticleWithAuthorBySlug(@Param("slug") String slug);

    Optional<Article> getArticleBySlug(String slug);

    //List<Article> getArticlesByTagsContainingAndUserUsernameAndFavoritedByListContaining(Set<Tag> tags, String user_username, Set<UserAccount> favoritedByList);

    @Query("""
                    SELECT a FROM Article a
                    WHERE (:tag IS NULL OR :tag IN (SELECT t.name FROM a.tags t))
                    AND (:author IS NULL OR a.user.username = :author)
                    AND (:favoritedBy IS NULL OR :favoritedBy IN (SELECT fl.username FROM a.favoritedByList fl))
                    ORDER BY a.createdAt DESC
                    """)
    List<Article> findByParams(@Param("tag") String tag,
            @Param("author") String author,
            @Param("favoritedBy") String favoritedBy,
            Pageable pageable);

    @Query("""
                    SELECT a FROM Article a
                    WHERE (:username IN (SELECT f.username FROM a.user.followers f))
                    ORDER BY a.createdAt DESC
                    """)
    List<Article> getFeed(@Param("username") String username,
                               Pageable pageable);

}
