package com.ai.sync_law.metadata.jpa;

import com.ai.sync_law.metadata.jpa.entity.BatchSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BatchSettingCommandRepository extends JpaRepository<BatchSettingEntity, Long> {
}
