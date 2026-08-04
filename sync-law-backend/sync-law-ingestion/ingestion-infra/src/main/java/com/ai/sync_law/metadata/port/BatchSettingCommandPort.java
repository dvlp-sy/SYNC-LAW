package com.ai.sync_law.metadata.port;

import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;

public interface BatchSettingCommandPort {
    BatchSetting updateBatchSetting(BatchType batchType, String query);
}
