package dev.biddan.springbootbatchexample;

import java.io.IOException;
import java.util.Optional;
import org.kohsuke.github.GHRepository;
import org.kohsuke.github.GitHub;
import org.kohsuke.github.GitHubBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class GithubClient {

    private final GitHub github;
    private final UserRepoMappingRepository userRepoMappingRepository;

    public GithubClient(
            @Value("${github.oauth-token}") String oauthToken,
            UserRepoMappingRepository userRepoMappingRepository) {
        try {
            this.github = new GitHubBuilder()
                    .withOAuthToken(oauthToken)
                    .build();
        } catch (IOException e) {
            throw new RuntimeException("깃허브 클라이언트 생성 실패");
        }

        this.userRepoMappingRepository = userRepoMappingRepository;
    }

    public GHRepository getRepository(String owner, String repositoryName) {
        String repositoryFullName = owner + "/" + repositoryName;

        try {
            return github.getRepository(repositoryFullName);
        } catch (IOException e) {
            throw new RuntimeException("저장소 조회 실패: " + repositoryFullName, e);
        }
    }
}
