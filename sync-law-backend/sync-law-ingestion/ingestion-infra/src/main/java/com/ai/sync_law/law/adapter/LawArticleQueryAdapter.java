package com.ai.sync_law.law.adapter;

import com.ai.sync_law.law.jpa.LawArticleQueryRepository;
import com.ai.sync_law.law.port.LawArticleQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class LawArticleQueryAdapter implements LawArticleQueryPort {

    private final LawArticleQueryRepository lawArticleQueryRepository;

    @Override
    public long getTotalCounts() {
        return lawArticleQueryRepository.count();
    }
}
