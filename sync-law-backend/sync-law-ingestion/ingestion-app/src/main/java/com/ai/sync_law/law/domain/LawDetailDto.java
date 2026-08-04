package com.ai.sync_law.law.domain;

import com.ai.sync_law.law.LawDetail;

import java.util.List;

public record LawDetailDto(
        String lawId,
        String lawKey,
        String lawName,
        String department,
        String enforceDate,
        List<ParsedArticle> articles
) {

    public record ParsedArticle(
            String articleKey,      // 조 키
            String articleNumber,   // 조 번호
            String articleContent   // 하위 항, 호, 목 내용을 하나로 결합한 텍스트 (RAG 청킹용)
    ) {}

    public static LawDetailDto from(LawDetail lawDetail) {
        List<ParsedArticle> parsedArticles = lawDetail.articles().stream()
                .map(article -> new ParsedArticle(
                        article.articleKey(),
                        article.articleNumber(),
                        article.articleContent()
                ))
                .toList();

        return new LawDetailDto(
                lawDetail.lawId(),
                lawDetail.lawKey(),
                lawDetail.lawName(),
                lawDetail.department(),
                lawDetail.enforceDate(),
                parsedArticles
        );
    }

    public LawDetail toLawDetail() {
        List<LawDetail.ParsedArticle> parsedArticles = this.articles().stream()
                .map(article -> new LawDetail.ParsedArticle(
                        article.articleKey(),
                        article.articleNumber(),
                        article.articleContent()
                ))
                .toList();

        return new LawDetail(
                this.lawId(),
                this.lawKey(),
                this.lawName(),
                this.department(),
                this.enforceDate(),
                parsedArticles
        );
    }
}
