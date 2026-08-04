package com.ai.sync_law.law.jpa;

import com.ai.sync_law.law.jpa.entity.LawArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LawArticleCommandRepository extends JpaRepository<LawArticleEntity, Long> {
}
