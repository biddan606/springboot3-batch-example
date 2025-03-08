package dev.biddan.springbootbatchexample;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHContent;
import org.kohsuke.github.GHRepository;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class CommitFileProcessor implements ItemProcessor<GHCommit, CommitFile> {

    private final GHRepository ghRepository;

    @Override
    public CommitFile process(GHCommit commit) throws IOException {
        if (commit == null) {
            return null;
        }
        GHCommit detailedCommit = ghRepository.getCommit(commit.getSHA1());

        return detailedCommit.listFiles().toList().stream()
                .filter(file -> file.getFileName().endsWith(".java"))
                .map(file -> {
                    try {
                        GHContent content = ghRepository.getFileContent(file.getFileName(), detailedCommit.getSHA1());

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(content.read(), StandardCharsets.UTF_8));
                        String fileContent = reader.lines().collect(Collectors.joining("\n"));

                        return CommitFile.builder()
                                .fileName(file.getFileName())
                                .fileContent(fileContent)
                                .username(ghRepository.getOwnerName())
                                .build();
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .findFirst().orElse(null);
    }
}
