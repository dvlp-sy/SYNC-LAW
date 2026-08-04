package com.ai.sync_law.metadata.adapter;

import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;
import com.ai.sync_law.metadata.jpa.BatchSettingQueryRepository;
import com.ai.sync_law.metadata.jpa.entity.BatchSettingEntity;
import com.ai.sync_law.metadata.port.BatchSettingQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchSettingQueryAdapter implements BatchSettingQueryPort {

    private final BatchSettingQueryRepository batchSettingQueryRepository;

    @Override
    public BatchSetting findByTypeAndQuery(BatchType batchType, String query) {
        return batchSettingQueryRepository.findByTypeAndQuery(batchType, query)
                .map(BatchSettingEntity::toBatchSetting)
                .orElseThrow(() -> new RuntimeException("BatchSetting not found for type: " + batchType + " and query: " + query));
    }

    @Override
    public List<BatchSetting> findAllByType(BatchType batchType) {
        return batchSettingQueryRepository.findAllByType(batchType).stream()
                .map(BatchSettingEntity::toBatchSetting)
                .toList();
    }
}
