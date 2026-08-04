package com.ai.sync_law.metadata;

public record BatchSetting(
        long id,
        BatchType batchType,
        String query,
        int page,
        int pageSize
) {
}
