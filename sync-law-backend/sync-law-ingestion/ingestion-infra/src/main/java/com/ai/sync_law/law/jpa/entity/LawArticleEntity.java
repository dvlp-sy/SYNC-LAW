package com.ai.sync_law.law.jpa.entity;

import com.ai.sync_law.law.LawDetail;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Table(name = "law_article")
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class LawArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Column(nullable = false, length = 20)
    private String lawId;

    @Column(nullable = false, length = 20)
    private String masterId;

    @Column(nullable = false, length = 100)
    private String lawName;

    @Column(nullable = false, length = 50)
    private String department;

    @Column(nullable = false, length = 10)
    private String enforceDate;

    @Column(nullable = false, length = 20)
    private String articleNo;

    @Column(length = 200)
    private String articleTitle;

    @Column(columnDefinition = "text")
    private String articleContent;

    public static List<LawArticleEntity> listFrom(LawDetail lawDetail) {
        if (lawDetail == null || lawDetail.articles() == null) {
            return List.of();
        }

        String cleanEnforceDate = lawDetail.enforceDate() != null
                ? lawDetail.enforceDate().replace("-", "").trim()
                : "";

        return lawDetail.articles().stream()
                .map(parsedArticle -> LawArticleEntity.builder()
                        .lawId(lawDetail.lawId())
                        .masterId(lawDetail.lawKey())
                        .lawName(lawDetail.lawName())
                        .department(lawDetail.department())
                        .enforceDate(cleanEnforceDate)
                        .articleNo(parsedArticle.articleNumber())
                        .articleTitle(parsedArticle.articleKey())
                        .articleContent(parsedArticle.articleContent())
                        .build())
                .toList();
    }
}
