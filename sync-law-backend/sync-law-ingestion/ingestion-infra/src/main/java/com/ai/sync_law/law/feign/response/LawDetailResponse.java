package com.ai.sync_law.law.feign.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record LawDetailResponse(
        @JsonProperty("법령") Law law
) {
    record Law(
            @JsonProperty("개정문") Amendment amendment,
            @JsonProperty("법령키") String lawKey,
            @JsonProperty("기본정보") BasicInfo basicInfo,
            @JsonProperty("부칙") Supplementary supplementary,
            @JsonProperty("조문") List<Article> articles
    ) {}

    // 개정문
    record Amendment(
            @JsonProperty("개정문내용") List<List<String>> amendmentContent
    ) {}

    // 기본 정보
    record BasicInfo(
            @JsonProperty("법령명_한글") String lawNameKorean,
            @JsonProperty("공포번호") String promulgationNumber,
            @JsonProperty("전화번호") String phoneNumber,
            @JsonProperty("언어") String language,
            @JsonProperty("제개정구분") String revisionType,
            @JsonProperty("법령ID") String lawId,
            @JsonProperty("공동부령정보") String jointOrdinanceInfo,
            @JsonProperty("소관부처") Department department,
            @JsonProperty("공포법령여부") String isPromulgated,
            @JsonProperty("법종구분") LawType lawType,
            @JsonProperty("제명변경여부") String isTitleChanged,
            @JsonProperty("시행일자") String enforcementDate,
            @JsonProperty("별표편집여부") String isAnnexEdited,
            @JsonProperty("연락부서") ContactDept contactDept,
            @JsonProperty("법령명_한자") String lawNameHanja,
            @JsonProperty("법령명약칭") String lawNameAbbreviation,
            @JsonProperty("공포일자") String promulgationDate,
            @JsonProperty("한글법령여부") String isKoreanLanguage,
            @JsonProperty("편장절관") String classificationCode
    ) {}

    record Department(
            @JsonProperty("content") String content,
            @JsonProperty("소관부처코드") String departmentCode
    ) {}

    record LawType(
            @JsonProperty("content") String content,
            @JsonProperty("법종구분코드") String lawTypeCode
    ) {}

    record ContactDept(
            @JsonProperty("부서단위") List<DeptDetail> departmentUnits
    ) {}

    record DeptDetail(
            @JsonProperty("부서연락처") String deptPhoneNumber,
            @JsonProperty("부서키") String deptKey,
            @JsonProperty("부서명") String deptName,
            @JsonProperty("소관부처명") String departmentName,
            @JsonProperty("소관부처코드") String departmentCode
    ) {}

    // 부칙
    record Supplementary(
            @JsonProperty("부칙단위") List<SuppDetail> supplementaryUnits
    ) {}

    record SuppDetail(
            @JsonProperty("부칙키") String suppKey,
            @JsonProperty("부칙공포일자") String suppPromulgationDate,
            @JsonProperty("부칙내용") List<List<String>> suppContent,
            @JsonProperty("부칙공포번호") String suppPromulgationNumber
    ) {}

    // 조문
    public record Article(
            @JsonProperty("조문번호") String articleNumber,
            @JsonProperty("조문시행일자") String articleEnforcementDate,
            @JsonProperty("조문변경여부") String isArticleChanged,
            @JsonProperty("조문이동이전") String articleMovedFrom,
            @JsonProperty("조문키") String articleKey,
            @JsonProperty("항") Paragraph paragraph
    ) {}

    public record Paragraph(
            @JsonProperty("호") List<Item> items
    ) {}

    public record Item(
            @JsonProperty("호번호") String itemNumber,
            @JsonProperty("호내용") String itemContent,
            @JsonProperty("목") List<SubItem> subItems
    ) {}

    public record SubItem(
            @JsonProperty("목번호") String subItemNumber,
            @JsonProperty("목내용") String subItemContent
    ) {}
}