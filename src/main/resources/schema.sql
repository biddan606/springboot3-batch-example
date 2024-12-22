DROP TABLE IF EXISTS user_repo_mappings;
CREATE TABLE user_repo_mappings
(
    id              BIGINT AUTO_INCREMENT,
    username        VARCHAR(100) NOT NULL,
    repository_name VARCHAR(200) NOT NULL,
    PRIMARY KEY (id)
);
