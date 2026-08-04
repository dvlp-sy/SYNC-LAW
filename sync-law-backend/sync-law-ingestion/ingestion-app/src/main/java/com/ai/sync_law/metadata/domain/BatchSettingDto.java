package com.ai.sync_law.metadata.domain;

import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;

public record BatchSettingDto(
        long id,
        BatchType batchType,
        String query,
        int page,
        int pageSize
) {

    public static BatchSettingDto from(BatchSetting batchSetting) {
        return new BatchSettingDto(
                batchSetting.id(),
                batchSetting.batchType(),
                batchSetting.query(),
                batchSetting.page(),
                batchSetting.pageSize()
        );
    }
}
