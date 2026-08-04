package com.ai.sync_law.metadata.jpa;

import com.ai.sync_law.metadata.BatchType;
import com.ai.sync_law.metadata.jpa.entity.BatchSettingEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BatchSettingQueryRepository extends JpaRepository<BatchSettingEntity, Long> {
    Optional<BatchSettingEntity> findByTypeAndQuery(BatchType batchType, String query);
    List<BatchSettingEntity> findAllByType(BatchType batchType);
}
