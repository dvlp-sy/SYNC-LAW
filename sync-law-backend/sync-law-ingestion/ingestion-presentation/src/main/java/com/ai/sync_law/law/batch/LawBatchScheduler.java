package com.ai.sync_law.law.batch;

import com.ai.sync_law.metadata.domain.BatchSettingDto;
import com.ai.sync_law.metadata.BatchSettingQueryService;
import com.ai.sync_law.metadata.domain.BatchTypeDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.parameters.JobParameters;
import org.springframework.batch.core.job.parameters.JobParametersBuilder;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
public class LawBatchScheduler {
    private final BatchSettingQueryService batchSettingQueryService;
    private final JobOperator jobOperator;
    private final Job lawJob;

    public LawBatchScheduler(BatchSettingQueryService batchSettingQueryService,
                             JobOperator jobOperator,
                             @Qualifier("lawJob") Job lawJob) {
        this.batchSettingQueryService = batchSettingQueryService;
        this.jobOperator = jobOperator;
        this.lawJob = lawJob;
    }

    @Scheduled(cron = "0 0 0 * * *")
    public void runLawJob() {
        try {
            List<BatchSettingDto> batchSettings = batchSettingQueryService.getBatchSettingsByType(BatchTypeDto.LAW);
            for (BatchSettingDto batchSetting : batchSettings) {
                String query = batchSetting.query();

                JobParameters jobParameters = new JobParametersBuilder()
                        .addString("query", query)
                        .addLong("time", System.currentTimeMillis())
                        .toJobParameters();

                log.info("BTCH:SCDL:STRT:::LAW job 시작 [query({})]", query);
                jobOperator.start(lawJob, jobParameters);
                log.info("BTCH:SCDL:CMPL:::LAW job 완료 [query({})]", query);
            }
        } catch (Exception e) {
            log.error("BTCH:SCDL:ERR_:::LAW job 실행 중 오류 발생 [msg({})]", e.getMessage());
        }
    }
}
