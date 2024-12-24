package dev.biddan.springbootbatchexample;

import java.io.IOException;
import java.util.List;
import org.kohsuke.github.GHCommit;
import org.kohsuke.github.GHRepository;
import org.springframework.batch.item.ExecutionContext;
import org.springframework.batch.item.ItemStreamException;
import org.springframework.batch.item.ItemStreamReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;

public class RepoCommitReader implements ItemStreamReader<RepoCommit> {

    private final GHRepository ghRepository;

    private List<GHCommit> repoCommits;
    private int index;

    public RepoCommitReader(GHRepository ghRepository) {
        this.ghRepository = ghRepository;
    }

    @Override
    public void open(ExecutionContext executionContext) throws ItemStreamException {
        ItemStreamReader.super.open(executionContext);

        try {
            repoCommits = ghRepository.listCommits()
                    .toList();
        } catch (IOException e) {
            throw new RuntimeException("커밋 목록 조회 실패", e);
        }

        index = 0;
    }

    @Override
    public RepoCommit read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        return null;
    }

    @Override
    public void update(ExecutionContext executionContext) throws ItemStreamException {
        ItemStreamReader.super.update(executionContext);
    }

    @Override
    public void close() throws ItemStreamException {
        ItemStreamReader.super.close();
    }
}
