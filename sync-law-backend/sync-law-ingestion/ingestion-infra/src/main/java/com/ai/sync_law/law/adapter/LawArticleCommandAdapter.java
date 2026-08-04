package com.ai.sync_law.law.adapter;

import com.ai.sync_law.law.jpa.LawArticleCommandRepository;
import com.ai.sync_law.law.jpa.entity.LawArticleEntity;
import com.ai.sync_law.law.port.LawArticleCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional
public class LawArticleCommandAdapter implements LawArticleCommandPort {

    private final LawArticleCommandRepository lawArticleCommandRepository;

    @Override
    public LawArticleEntity save(LawArticleEntity lawArticleEntity) {
        return lawArticleCommandRepository.save(lawArticleEntity);
    }

    @Override
    public List<LawArticleEntity> saveAll(Iterable<LawArticleEntity> lawArticleEntities) {
        return lawArticleCommandRepository.saveAll(lawArticleEntities);
    }
}
