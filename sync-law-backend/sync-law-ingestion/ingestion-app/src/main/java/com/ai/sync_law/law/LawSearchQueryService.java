package com.ai.sync_law.law;

import com.ai.sync_law.law.domain.LawArticlesDto;
import com.ai.sync_law.law.domain.LawDetailDto;
import com.ai.sync_law.law.port.LawArticleQueryPort;
import com.ai.sync_law.law.port.LawFetchPort;
import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;
import com.ai.sync_law.metadata.port.BatchSettingQueryPort;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class LawSearchQueryService {

    private static final int MAX_LAW_ARTICLE_COUNT = 5000;

    private final LawFetchPort lawFetchPort;
    private final LawArticleQueryPort lawArticleQueryPort;
    private final BatchSettingQueryPort batchSettingQueryPort;

    public LawArticlesDto fetchNextPageOfLawSearchResults(String query) {
        if (lawArticleQueryPort.getTotalCounts() > MAX_LAW_ARTICLE_COUNT) {
            return null;
        }
        BatchSetting setting = batchSettingQueryPort.findByTypeAndQuery(BatchType.LAW, query);
        return LawArticlesDto.from(lawFetchPort.fetchLawSearchResults(query, setting.page(), setting.pageSize()));
    }

    public LawDetailDto fetchLawDetail(String masterId) {
        return LawDetailDto.from(lawFetchPort.fetchLawDetail(masterId));
    }
}
