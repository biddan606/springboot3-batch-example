package dev.biddan.springbootbatchexample;

import java.time.LocalDateTime;

public record RepoCommit(
        UserRepoMapping userRepoMapping,
        String commitHash,
        String commitMessage,
        LocalDateTime commitTime
) {

}
