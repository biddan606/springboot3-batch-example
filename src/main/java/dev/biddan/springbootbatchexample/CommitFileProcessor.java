package dev.biddan.springbootbatchexample;

import dev.biddan.springbootbatchexample.CommitFileProcessor.CommitFileInfo;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHCommit.File;
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

        List<String> changedFiles = detailedCommit.listFiles().toList().stream()
                .map(File::getFileName)
                .toList();

        return new CommitFileInfo(detailedCommit.getSHA1(), changedFiles);
    }

    public record CommitFileInfo(
            String sha,
            List<String> changedFiles
    ) {

    }
}
