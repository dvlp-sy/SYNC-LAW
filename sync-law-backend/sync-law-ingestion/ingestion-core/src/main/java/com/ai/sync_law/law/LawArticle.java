package com.ai.sync_law.law;

public record LawArticle(
        String lawId,                // 법령ID
        String lawSequenceNumber,    // 법령일련번호 MST
        String lawNameKorean,        // 법령명 한글
        String departmentName,       // 소관부처명
        String enforcementDate,      // 시행일자 ("2026-07-08")
        String lawDetailLink         // 상세 링크 URL
) {}
