package com.ai.sync_law.law;

import com.ai.sync_law.law.domain.LawDetailDto;
import com.ai.sync_law.law.jpa.entity.LawArticleEntity;
import com.ai.sync_law.law.port.LawArticleCommandPort;
import com.ai.sync_law.law.port.LawArticleQueryPort;
import com.ai.sync_law.metadata.BatchType;
import com.ai.sync_law.metadata.port.BatchSettingCommandPort;
import lombok.RequiredArgsConstructor;
import com.ai.sync_law.law.port.LawFetchPort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class LawSearchCommandService {
    private static final int MAX_LAW_ARTICLE_COUNT = 5000;
    private static final int DISPLAY_COUNT = 10;

    private final LawFetchPort lawFetchPort;
    private final LawArticleQueryPort lawArticleQueryPort;
    private final LawArticleCommandPort lawArticleCommandPort;
    private final BatchSettingCommandPort batchSettingCommandPort;

    public void saveLawArticles(String query, List<LawDetailDto> lawDetailDtoList) {
        lawDetailDtoList.forEach(lawDetailDto -> {
            // 총 법령 조문 수 확인
            if (lawArticleQueryPort.getTotalCounts() > MAX_LAW_ARTICLE_COUNT) {
                return;
            }
            // 법령 조문 저장
            lawArticleCommandPort.saveAll(LawArticleEntity.listFrom(lawDetailDto.toLawDetail()));
            // 배치 테이블 업데이트
            batchSettingCommandPort.updateBatchSetting(BatchType.LAW, query);
        });


    }

    public void searchLawAndSave(String query) {
        int currentPage = 1;
        int totalCounts = 100;

        while (currentPage * DISPLAY_COUNT <= totalCounts) {
            LawArticles lawArticles = lawFetchPort.fetchLawSearchResults(query, currentPage, DISPLAY_COUNT);
            totalCounts = Math.min(totalCounts, lawArticles.totalCounts());

            for (LawArticle lawArticle : lawArticles.lawArticleList()) {
                // 법령 상세 정보 API 호출
                LawDetail lawDetail = lawFetchPort.fetchLawDetail(lawArticle.lawSequenceNumber());
                // 법령 조문 저장
                lawArticleCommandPort.saveAll(LawArticleEntity.listFrom(lawDetail));
            }

            currentPage++;
        }
    }
}
