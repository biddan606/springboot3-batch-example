package dev.biddan.springbootbatchexample;

import dev.biddan.springbootbatchexample.CommitFileProcessor.CommitFileInfo;
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
public class CommitFileProcessor implements ItemProcessor<GHCommit, CommitFileInfo> {

    private final GHRepository ghRepository;

    @Override
    public CommitFileInfo process(GHCommit commit) throws IOException {
        if (commit == null) {
            return null;
        }
        GHCommit detailedCommit = ghRepository.getCommit(commit.getSHA1());

        List<ChangeFileInfo> changedFiles = detailedCommit.listFiles().toList().stream()
                .map(file -> {
                    try {
                        GHContent content = ghRepository.getFileContent(file.getFileName(), detailedCommit.getSHA1());

                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(content.read(), StandardCharsets.UTF_8));
                        String fileContent = reader.lines().collect(Collectors.joining("\n"));

                        return new ChangeFileInfo(file.getFileName(), fileContent);
                    } catch (IOException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .toList();

        return new CommitFileInfo(detailedCommit.getSHA1(), changedFiles);
    }

    public record CommitFileInfo(
            String sha,
            List<ChangeFileInfo> changedFiles
    ) {

    }

    public record ChangeFileInfo(
            String fileName,
            String fileContent) {

    }
}
