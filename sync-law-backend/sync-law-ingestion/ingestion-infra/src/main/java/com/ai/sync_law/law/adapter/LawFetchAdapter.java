package com.ai.sync_law.law.adapter;

import com.ai.sync_law.law.LawDetail;
import com.ai.sync_law.law.LawArticles;
import com.ai.sync_law.law.feign.LawApiClient;
import com.ai.sync_law.law.feign.response.LawDetailResponse;
import com.ai.sync_law.law.port.LawFetchPort;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
class LawFetchAdapter implements LawFetchPort {

    private static final String TARGET = "law";
    private static final String RESPONSE_TYPE = "JSON";

    private final String apiKey;
    private final LawApiClient lawApiClient;

    public LawFetchAdapter(
            @Value("${infra.national-law.key}") String apiKey,
            LawApiClient lawApiClient
    ) {
        this.apiKey = apiKey;
        this.lawApiClient = lawApiClient;
    }

    @Override
    public LawArticles fetchLawSearchResults(String query, int page, int display) {
        return lawApiClient
                .getLawArticles(apiKey, TARGET, RESPONSE_TYPE, query, display, page)
                .toLawArticles();
    }

    @Override
    public LawDetail fetchLawDetail(String masterId) {
        LawDetailResponse response = lawApiClient
                .getLawDetail(apiKey, TARGET, RESPONSE_TYPE, masterId);
        return response.toLawDetail();
    }
}
