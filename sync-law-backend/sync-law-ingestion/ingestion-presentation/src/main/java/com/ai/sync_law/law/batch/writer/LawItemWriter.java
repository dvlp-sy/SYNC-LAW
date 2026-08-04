package com.ai.sync_law.law.batch.writer;

import com.ai.sync_law.law.LawSearchCommandService;
import com.ai.sync_law.law.domain.LawDetailDto;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.infrastructure.item.Chunk;
import org.springframework.batch.infrastructure.item.ItemWriter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
@StepScope
public class LawItemWriter implements ItemWriter<LawDetailDto> {

    private final LawSearchCommandService lawSearchCommandService;
    private final String query;

    public LawItemWriter(
            LawSearchCommandService lawSearchCommandService,
            @Value("#{jobParameters['query']}") String query
    ) {
        this.lawSearchCommandService = lawSearchCommandService;
        this.query = query;
    }

    @Override
    public void write(Chunk<? extends LawDetailDto> chunk) {
        List<LawDetailDto> items = new ArrayList<>(chunk.getItems());
        lawSearchCommandService.saveLawArticles(query, items);
    }
}
