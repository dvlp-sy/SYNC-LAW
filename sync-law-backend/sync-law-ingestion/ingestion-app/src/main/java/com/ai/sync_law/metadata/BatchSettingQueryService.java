package com.ai.sync_law.metadata;

import com.ai.sync_law.metadata.domain.BatchSettingDto;
import com.ai.sync_law.metadata.domain.BatchTypeDto;
import com.ai.sync_law.metadata.port.BatchSettingQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BatchSettingQueryService {

    private final BatchSettingQueryPort batchSettingQueryPort;

    public List<BatchSettingDto> getBatchSettingsByType(BatchTypeDto batchTypeDto) {
        return batchSettingQueryPort.findAllByType(batchTypeDto.toDomain()).stream()
                .map(BatchSettingDto::from)
                .toList();
    }
}
