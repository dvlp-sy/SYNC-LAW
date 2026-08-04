package com.ai.sync_law.law.batch.reader;

import com.ai.sync_law.law.LawSearchQueryService;
import com.ai.sync_law.law.domain.LawArticleDto;
import com.ai.sync_law.law.domain.LawArticlesDto;
import com.ai.sync_law.law.domain.LawDetailDto;
import org.jspecify.annotations.Nullable;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.ItemReader;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

@Component
@StepScope
public class LawItemReader implements ItemReader<LawDetailDto> {

    private static final String DEFAULT_QUERY = "미검색";

    private final LawSearchQueryService lawSearchQueryService;
    private final Queue<LawDetailDto> buffer = new ConcurrentLinkedQueue<>();
    private final String query;

    public LawItemReader(
            LawSearchQueryService lawSearchQueryService,
            @Value("#{jobParameters['query']}") String query
    ) {
        this.lawSearchQueryService = lawSearchQueryService;
        this.query = (query != null) ? query : DEFAULT_QUERY;
    }

    @Override
    public @Nullable LawDetailDto read() {
        if (!buffer.isEmpty()) {
            return buffer.poll();
        }
        if (query.equals(DEFAULT_QUERY)) {
            return null;
        }

        // Batch Setting 테이블에 저장된 페이지부터 법령 조회 API 호출
        LawArticlesDto nextArticles = lawSearchQueryService.fetchNextPageOfLawSearchResults(query);

        if (nextArticles == null || nextArticles.lawArticleList().isEmpty()) {
            return null;
        }

        for (LawArticleDto article : nextArticles.lawArticleList()) {
            LawDetailDto lawDetail = lawSearchQueryService.fetchLawDetail(article.lawSequenceNumber());
            buffer.offer(lawDetail);
        }
        return buffer.poll();
    }
}
