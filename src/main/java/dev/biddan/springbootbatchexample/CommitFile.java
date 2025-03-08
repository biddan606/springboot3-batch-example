package dev.biddan.springbootbatchexample;

import lombok.Builder;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "commit_files")
@Builder
public record CommitFile(
        String fileName,
        String fileContent,
        String username
) {

}
