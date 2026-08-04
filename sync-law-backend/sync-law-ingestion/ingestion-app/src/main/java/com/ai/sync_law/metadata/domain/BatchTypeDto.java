package com.ai.sync_law.metadata.domain;

import com.ai.sync_law.metadata.BatchType;

public enum BatchTypeDto {
    LAW,
    PRECEDENT,
    ;

    public BatchType toDomain() {
        return switch (this) {
            case LAW -> BatchType.LAW;
            case PRECEDENT -> BatchType.PRECEDENT;
        };
    }
}
