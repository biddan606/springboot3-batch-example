package dev.biddan.springbootbatchexample;

import lombok.Builder;
import org.springframework.data.relational.core.mapping.Table;

@Table("user_repo_mappings")
@Builder
public record UserRepoMapping(
        Long id,
        String username,
        String repositoryName
) {

}
