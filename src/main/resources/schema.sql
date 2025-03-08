DROP TABLE IF EXISTS user_repo_mappings;
CREATE TABLE user_repo_mappings
(
    id              BIGINT AUTO_INCREMENT,
    username        VARCHAR(100) NOT NULL,
    repository_name VARCHAR(200) NOT NULL,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS commit_files;
CREATE TABLE commit_files
(
    id          BIGINT AUTO_INCREMENT,
    file_name   VARCHAR(500)  NOT NULL,
    file_content CLOB NOT NULL,
    PRIMARY KEY (id)
)
