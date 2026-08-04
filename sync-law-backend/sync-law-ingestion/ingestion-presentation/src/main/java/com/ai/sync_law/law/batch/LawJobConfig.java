package com.ai.sync_law.law.batch;

import com.ai.sync_law.law.batch.reader.LawItemReader;
import com.ai.sync_law.law.batch.writer.LawItemWriter;
import com.ai.sync_law.law.domain.LawDetailDto;
import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.configuration.annotation.EnableJdbcJobRepository;
import org.springframework.batch.core.job.Job;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.Step;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
@EnableJdbcJobRepository
public class LawJobConfig {

    private final LawItemReader lawItemReader;
    private final LawItemWriter lawItemWriter;

    @Bean
    public Job lawJob(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new JobBuilder("lawJob", jobRepository)
                .start(lawStep(jobRepository, transactionManager))
                .build();
    }

    @Bean
    public Step lawStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("lawStep", jobRepository)
                .<LawDetailDto, LawDetailDto>chunk(10).transactionManager(transactionManager)
                .reader(lawItemReader)
                .writer(lawItemWriter)
                .build();
    }
}
