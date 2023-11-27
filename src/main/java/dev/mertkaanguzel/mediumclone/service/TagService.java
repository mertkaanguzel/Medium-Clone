package dev.mertkaanguzel.mediumclone.service;

import dev.mertkaanguzel.mediumclone.model.Article;
import dev.mertkaanguzel.mediumclone.model.Tag;
import dev.mertkaanguzel.mediumclone.repository.TagRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TagService {
    private final TagRepository tagRepository;

    public TagService(TagRepository tagRepository) {
        this.tagRepository = tagRepository;
    }

    public List<Tag> createTags(List<String> tagNames) {
        /*
        return tagNames.stream()
                .map(name -> tagRepository.saveAndFlush(new Tag(name)))
                .toList();

         */
        Set<Tag> tagList = new HashSet<>();
        tagNames.forEach(name -> tagList.add(new Tag(name)));

        return tagRepository.saveAllAndFlush(tagList);

    }

    public List<String> getTags() {
        return tagRepository.findAll().stream().map(Tag::getName).toList();
    }
}
