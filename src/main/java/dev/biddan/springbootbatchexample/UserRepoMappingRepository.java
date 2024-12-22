package dev.biddan.springbootbatchexample;

import java.util.List;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepoMappingRepository {

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public Optional<UserRepoMapping> findByUsername(String username) {
        String sql = """
                SELECT * 
                FROM user_repo_mappings
                WHERE username = :username
                """;

        MapSqlParameterSource params = new MapSqlParameterSource("username", username);

        List<UserRepoMapping> result = namedParameterJdbcTemplate.query(
                sql,
                params,
                (rs, rowNum) -> UserRepoMapping.builder()
                        .id(rs.getLong("id"))
                        .username(rs.getString("username"))
                        .repositoryName(rs.getString("repository_name"))
                        .build());

        if (result.isEmpty()) {
            return Optional.empty();
        }
        return Optional.of(result.get(0));
    }
}
