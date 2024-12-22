package dev.biddan.springbootbatchexample;

import lombok.RequiredArgsConstructor;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CommitAnalysisConfig {

    @Bean
    Job commitAnalysisJob(JobRepository jobRepository, Step commitAnalysisStep) {
        return new JobBuilder("commitAnalysisJob", jobRepository)
                .start(commitAnalysisStep)
                .build();
    }

    @Bean
    Step commitAnalysisStep(
            JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("commitAnalysisStep", jobRepository)
                .<String, String>chunk(10, transactionManager)
                .reader(() -> null)
                .processor(s -> "")
                .writer(s -> System.out.println("write commit"))
                .build();
    }
}
