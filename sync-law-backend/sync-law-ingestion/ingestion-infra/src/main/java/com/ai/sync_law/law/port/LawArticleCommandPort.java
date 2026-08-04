package com.ai.sync_law.law.port;

import com.ai.sync_law.law.jpa.entity.LawArticleEntity;

import java.util.List;

public interface LawArticleCommandPort {
    LawArticleEntity save(LawArticleEntity lawArticleEntity);
    List<LawArticleEntity> saveAll(Iterable<LawArticleEntity> lawArticleEntities);
}
