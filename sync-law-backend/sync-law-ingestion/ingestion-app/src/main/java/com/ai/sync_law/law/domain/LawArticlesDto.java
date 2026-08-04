package com.ai.sync_law.law.domain;

import com.ai.sync_law.law.LawArticles;

import java.util.List;

public record LawArticlesDto (
        List<LawArticleDto> lawArticleList,
        int totalCounts
) {
    public static LawArticlesDto from(LawArticles lawArticles) {
        return new LawArticlesDto(
                lawArticles.lawArticleList().stream()
                        .map(LawArticleDto::from)
                        .toList(),
                lawArticles.totalCounts()
        );
    }
}