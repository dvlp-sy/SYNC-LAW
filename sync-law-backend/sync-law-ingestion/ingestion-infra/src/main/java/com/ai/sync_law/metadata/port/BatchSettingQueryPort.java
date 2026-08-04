package com.ai.sync_law.metadata.port;

import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;

import java.util.List;

public interface BatchSettingQueryPort {
    BatchSetting findByTypeAndQuery(BatchType batchType, String query);
    List<BatchSetting> findAllByType(BatchType batchType);
}
