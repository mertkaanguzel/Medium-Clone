package dev.mertkaanguzel.mediumclone.repository;

import dev.mertkaanguzel.mediumclone.model.Comment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface CommentRepository extends JpaRepository<Comment, Long> {
    List<Comment> getAllByArticleSlug(String slug);
    Optional<Comment> getByArticleSlugAndId(String slug, Long id);
}
