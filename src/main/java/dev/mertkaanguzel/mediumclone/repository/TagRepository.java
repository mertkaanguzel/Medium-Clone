package dev.mertkaanguzel.mediumclone.repository;

import dev.mertkaanguzel.mediumclone.model.Tag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TagRepository extends JpaRepository<Tag, String/*Long*/> {
}
