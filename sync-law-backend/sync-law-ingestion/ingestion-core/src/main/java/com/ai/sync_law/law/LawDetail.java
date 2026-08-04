package com.ai.sync_law.law;

import java.util.List;

public record LawDetail(
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
}
