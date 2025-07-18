CREATE TABLE attachment
(
    id                 BIGINT AUTO_INCREMENT NOT NULL,
    created_at         DATETIME(6) NULL,
    modified_at         DATETIME(6) NULL,
    post_id            BIGINT NULL,
    file_key           VARCHAR(255) NULL,
    thumbnail_url      VARCHAR(255) NULL,
    original_name      VARCHAR(255) NULL,
    is_video           TINYINT(1) NULL,
    attachment_status  ENUM('UPLOADING', 'UPLOADED', 'FAILED') NULL,
    size               BIGINT NULL,
    CONSTRAINT pk_attachment PRIMARY KEY (id)
);

CREATE INDEX idx_file_key ON attachment (file_key);
