package com.ai.sync_law.law.jpa;

import com.ai.sync_law.law.jpa.entity.LawArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LawArticleQueryRepository extends JpaRepository<LawArticleEntity, Long> {
}
