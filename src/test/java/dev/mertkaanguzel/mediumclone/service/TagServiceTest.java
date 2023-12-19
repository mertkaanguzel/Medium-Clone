package dev.mertkaanguzel.mediumclone.service;


import dev.mertkaanguzel.mediumclone.dto.CommentDto;
import dev.mertkaanguzel.mediumclone.model.Tag;
import dev.mertkaanguzel.mediumclone.repository.TagRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TagServiceTest {
    private TagRepository tagRepository;
    private TagService tagService;

    private final Tag tag = new Tag("reactjs");

    @BeforeEach
    void setUp() {
        tagRepository = mock(TagRepository.class);
        tagService = new TagService(tagRepository);
    }


    @Test
    void testCreateTags_shouldCreateTags() {
        when(tagRepository.saveAllAndFlush(anyIterable())).thenReturn(List.of(tag));

        List<Tag> result = tagService.createTags(List.of("reactjs"));
        assertEquals(List.of(tag), result);
    }

    @Test
    void testGetTags_shouldReturnListOfStrings() {
        when(tagRepository.findAll()).thenReturn(List.of(tag));
        List<String> result = tagService.getTags();
        assertEquals(List.of("reactjs"), result);
    }
}
