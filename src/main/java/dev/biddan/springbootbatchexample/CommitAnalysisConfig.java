package dev.biddan.springbootbatchexample;

import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.JobScope;
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
            PlatformTransactionManager transactionManager) {
        return new StepBuilder("commitAnalysisStep", jobRepository)
                .<RepoCommit, String>chunk(10, transactionManager)
                .reader(repoCommitReader(null, null))
                .processor(commit -> null)
                .writer(s -> System.out.println("write commit"))
                .build();
    }

    @Bean
    @StepScope
    RepoCommitReader repoCommitReader(
            @Value("#{jobParameters['username']}") String username,
            @Value("#{jobParameters['repositoryName']}") String repositoryName) {
        GHRepository ghRepository = githubClient.getRepository(username, repositoryName);
        return new RepoCommitReader(ghRepository);
    }
}
