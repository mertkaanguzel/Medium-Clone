package dev.mertkaanguzel.mediumclone.dto;

import java.util.List;

public record ArticleListDto(List<ArticleDto> articles, Integer articlesCount) {
}
