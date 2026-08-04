package com.ai.sync_law.metadata.adapter;

import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;
import com.ai.sync_law.metadata.jpa.BatchSettingCommandRepository;
import com.ai.sync_law.metadata.jpa.BatchSettingQueryRepository;
import com.ai.sync_law.metadata.jpa.entity.BatchSettingEntity;
import com.ai.sync_law.metadata.port.BatchSettingCommandPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
@Transactional
public class BatchSettingCommandAdapter implements BatchSettingCommandPort {

    private final BatchSettingQueryRepository batchSettingQueryRepository;
    private final BatchSettingCommandRepository batchSettingCommandRepository;

    @Override
    public BatchSetting updateBatchSetting(BatchType batchType, String query) {
        BatchSettingEntity batchSettingEntity = batchSettingQueryRepository.findByTypeAndQuery(batchType, query)
                .orElseThrow(() -> new IllegalArgumentException("BatchSetting not found for type: " + batchType + " and query: " + query));
        batchSettingEntity.setPage(batchSettingEntity.getPage() + 1);
        batchSettingCommandRepository.save(batchSettingEntity);
        return batchSettingEntity.toBatchSetting();
    }
}
