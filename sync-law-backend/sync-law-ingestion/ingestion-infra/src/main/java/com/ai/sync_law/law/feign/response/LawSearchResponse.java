package com.ai.sync_law.law.feign.response;

import com.ai.sync_law.law.LawArticle;
import com.ai.sync_law.law.LawArticles;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

public record LawSearchResponse(
        @JsonProperty("LawSearch") LawSearchStatus lawSearchStatus
) {
    public record LawSearchStatus(
            @JsonProperty("law") List<LawSearchResult> lawList,
            @JsonProperty("totalCnt") String totalCount,
            @JsonProperty("numOfRows") String numberOfRows,
            @JsonProperty("키워드") String query
    ) {}

    public record LawSearchResult(
            @JsonProperty("id") String id,
            @JsonProperty("법령ID") String lawId,
            @JsonProperty("법령일련번호") String lawSequenceNumber,
            @JsonProperty("법령명한글") String lawNameKorean,
            @JsonProperty("법령약칭명") String lawAbbreviation,
            @JsonProperty("법령구분명") String lawCategoryName,
            @JsonProperty("현행연혁코드") String currentHistoryCode,
            @JsonProperty("소관부처코드") String departmentCode,
            @JsonProperty("소관부처명") String departmentName,
            @JsonProperty("제개정구분명") String amendmentTypeName,
            @JsonProperty("공포일자") String promulgationDate,
            @JsonProperty("공포번호") String promulgationNumber,
            @JsonProperty("시행일자") String enforcementDate,
            @JsonProperty("법령상세링크") String lawDetailLink,
            @JsonProperty("자법타법여부") String childOtherLawStatus,
            @JsonProperty("공동부령정보") String jointMinisterialDecreeInfo
    ) {}

    public LawArticles toLawArticles() {
        if (lawSearchStatus == null || lawSearchStatus.lawList() == null) {
            return new LawArticles(List.of(), 0);
        }
        return new LawArticles(lawSearchStatus.lawList.stream()
                .map(lawSearchResult -> new LawArticle(
                        lawSearchResult.lawId(),
                        lawSearchResult.lawSequenceNumber(),
                        lawSearchResult.lawNameKorean(),
                        lawSearchResult.departmentName(),
                        lawSearchResult.enforcementDate(),
                        lawSearchResult.lawDetailLink()))
                .toList(),
                Integer.parseInt(lawSearchStatus.totalCount()));
    }
}