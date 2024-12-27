package dev.biddan.springbootbatchexample;

import dev.biddan.springbootbatchexample.CommitFileProcessor.CommitFileInfo;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.builder.StepBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;

@Configuration
@RequiredArgsConstructor
public class CommitAnalysisConfig {

    private final GithubClient githubClient;

    @Bean
    Job commitAnalysisJob(JobRepository jobRepository, Step commitAnalysisStep) {
        return new JobBuilder("commitAnalysisJob", jobRepository)
                .start(commitAnalysisStep)
                .build();
    }

    @Bean
    Step commitAnalysisStep(
            JobRepository jobRepository,
            PlatformTransactionManager transactionManager,
            RepoCommitReader reader,
            CommitFileProcessor processor
            ) {
        return new StepBuilder("commitAnalysisStep", jobRepository)
                .<GHCommit, CommitFileInfo>chunk(10, transactionManager)
                .reader(reader)
                .processor(processor)
                .writer(System.out::println)
                .build();
    }

    @Bean
    @StepScope
    GHRepository ghRepository(
            @Value("#{jobParameters['username']}") String username,
            @Value("#{jobParameters['repositoryName']}") String repositoryName) {
        return githubClient.getRepository(username, repositoryName);
    }

    @Bean
    @StepScope
    RepoCommitReader repoCommitReader(GHRepository ghRepository){
        return new RepoCommitReader(ghRepository);
    }

    @Bean
    CommitFileProcessor commitFileProcessor(GHRepository ghRepository) {
        return new CommitFileProcessor(ghRepository);
    }
}
