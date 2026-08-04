package com.ai.sync_law.metadata.jpa.entity;

import com.ai.sync_law.metadata.BatchSetting;
import com.ai.sync_law.metadata.BatchType;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Table(
        name = "batch_setting",
        uniqueConstraints = {
                @UniqueConstraint(name = "batch_setting_type_query_uk", columnNames = {"type", "query"})
})
@Builder(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class BatchSettingEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Long id;

    @Enumerated(EnumType.STRING)
    private BatchType type;

    @Column(nullable = false, columnDefinition = "text")
    private String query;

    @Setter
    @Column(nullable = false)
    private Integer page;

    @Column(nullable = false)
    private Integer pageSize;

    public BatchSetting toBatchSetting() {
        return new BatchSetting(
                this.id,
                this.type,
                this.query,
                this.page,
                this.pageSize
        );
    }
}
