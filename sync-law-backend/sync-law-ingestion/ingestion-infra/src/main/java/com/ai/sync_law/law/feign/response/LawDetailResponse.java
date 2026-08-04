package com.ai.sync_law.law.feign.response;

import com.ai.sync_law.law.LawDetail;
import com.ai.sync_law.law.LawDetail.ParsedArticle;
import com.fasterxml.jackson.annotation.JsonFormat;
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
            @JsonProperty("조문") ArticleWrapper articleWrapper
    ) {}

    // 개정문
    record Amendment(
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
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
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
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
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            @JsonProperty("부칙단위") List<SuppDetail> supplementaryUnits
    ) {}

    record SuppDetail(
            @JsonProperty("부칙키") String suppKey,
            @JsonProperty("부칙공포일자") String suppPromulgationDate,
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            @JsonProperty("부칙내용") List<List<String>> suppContent,
            @JsonProperty("부칙공포번호") String suppPromulgationNumber
    ) {}

    // 조문
    record ArticleWrapper(
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            @JsonProperty("조문단위") List<Article> articles
    ) {}

    record Article(
            @JsonProperty("조문번호") String articleNumber,
            @JsonProperty("조문시행일자") String articleEnforcementDate,
            @JsonProperty("조문변경여부") String isArticleChanged,
            @JsonProperty("조문이동이전") String articleMovedFrom,
            @JsonProperty("조문키") String articleKey,
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            @JsonProperty("항") List<Paragraph> paragraphs
    ) {}

    record Paragraph(
            @JsonProperty("항번호") String paragraphNumber,
            @JsonProperty("항내용") String paragraphContent,
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            @JsonProperty("호") List<Item> items
    ) {}

    record Item(
            @JsonProperty("호번호") String itemNumber,
            @JsonProperty("호내용") String itemContent,
            @JsonFormat(with = JsonFormat.Feature.ACCEPT_SINGLE_VALUE_AS_ARRAY)
            @JsonProperty("목") List<SubItem> subItems
    ) {}

    record SubItem(
            @JsonProperty("목번호") String subItemNumber,
            @JsonProperty("목내용") String subItemContent
    ) {}

    public LawDetail toLawDetail() {
        // 조문 텍스트 변환
        List<ParsedArticle> parsedArticles = law.articleWrapper().articles.stream()
                .map(article -> new ParsedArticle(
                        article.articleKey(),
                        article.articleNumber(),
                        flattenParagraphs(article.paragraphs())
                ))
                .toList();
        return new LawDetail(
                law.basicInfo().lawId(),
                law.lawKey(),
                law.basicInfo().lawNameKorean(),
                law.basicInfo().department().content(),
                law.basicInfo().enforcementDate(),
                parsedArticles
        );
    }

    /**
     * 항 -> 호 -> 목 계층 문자열을 RAG 검색 최적화를 위한 단일 텍스트로 변환하는 메서드
     */
    private static String flattenParagraphs(List<Paragraph> paragraphs) {
        // 항이 null이거나 호가 비어있는 경우 빈 문자열 반환
        if (paragraphs == null || paragraphs.isEmpty()) {
            return "";
        }

        // 호의 세부 내용을 단일 문자열로 결합
        StringBuilder sb = new StringBuilder();
        for (Paragraph paragraph : paragraphs) {
            flattenParagraph(paragraph, sb);
            if (paragraph.items() != null && !paragraph.items().isEmpty()) {
                for (Item item : paragraph.items()) {
                    flattenItem(item, sb);
                    flattenSubItemList(item, sb);
                }
            }
        }

        // 최종 문자열 반환 (양쪽 공백 제거)
        return sb.toString().trim();
    }

    private static void flattenParagraph(Paragraph paragraph, StringBuilder sb) {
        if (paragraph == null) {
            return ;
        }
        if (paragraph.paragraphNumber() != null) {
            sb.append(paragraph.paragraphNumber()).append(" ");
        }
        if (paragraph.paragraphContent() != null) {
            sb.append(paragraph.paragraphContent()).append("\n");
        }
    }

    private static void flattenSubItemList(Item item, StringBuilder sb) {
        if (item == null || item.subItems() == null || item.subItems().isEmpty()) {
            return ;
        }
        item.subItems().forEach(subItem -> {
            if (subItem.subItemNumber() != null) {
                sb.append("  ").append(subItem.subItemNumber()).append(" ");
            }
            if (subItem.subItemContent() != null) {
                sb.append(subItem.subItemContent()).append("\n");
            }
        });
    }

    private static void flattenItem(Item item, StringBuilder sb) {
        if (item == null) {
            return ;
        }
        if (item.itemNumber() != null) {
            sb.append(item.itemNumber()).append(" ");
        }
        if (item.itemContent() != null) {
            sb.append(item.itemContent()).append("\n");
        }
    }
}