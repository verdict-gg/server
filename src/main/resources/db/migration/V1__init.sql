CREATE TABLE attachment
(
    id                BIGINT AUTO_INCREMENT NOT NULL,
    created_at        datetime              NULL,
    updated_at        datetime              NULL,
    deleted_at        datetime              NULL,
    post_id           BIGINT                NULL,
    file_key          VARCHAR(255)          NULL,
    thumbnail_url     VARCHAR(255)          NULL,
    original_name     VARCHAR(255)          NULL,
    is_video          BIT(1)                NULL,
    attachment_status VARCHAR(255)          NULL,
    size              BIGINT                NULL,
    CONSTRAINT pk_attachment PRIMARY KEY (id)
);

CREATE TABLE comment
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    deleted_at datetime              NULL,
    user_id    BIGINT                NULL,
    post_id    BIGINT                NULL,
    content    VARCHAR(255)          NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE post
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    deleted_at datetime              NULL,
    user_id    BIGINT                NULL,
    title      VARCHAR(255)          NULL,
    content    VARCHAR(255)          NULL,
    status     VARCHAR(255)          NULL,
    closed_at  datetime              NULL,
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE signout
(
    signout_id BIGINT AUTO_INCREMENT NOT NULL,
    identifier VARCHAR(255)          NULL,
    reason     VARCHAR(255)          NULL,
    signout_at datetime              NULL,
    CONSTRAINT pk_signout PRIMARY KEY (signout_id)
);

CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime              NULL,
    updated_at    datetime              NULL,
    deleted_at    datetime              NULL,
    user_role     VARCHAR(100)          NOT NULL,
    identifier    VARCHAR(1000)         NOT NULL,
    provider_info VARCHAR(255)          NOT NULL,
    email         VARCHAR(255)          NULL,
    nickname      VARCHAR(20)           NULL,
    information   VARCHAR(100)          NULL,
    image_url     VARCHAR(1000)         NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE vote
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime              NULL,
    updated_at     datetime              NULL,
    deleted_at     datetime              NULL,
    post_id        BIGINT                NULL,
    vote_option_id BIGINT                NULL,
    CONSTRAINT pk_vote PRIMARY KEY (id)
);

CREATE TABLE vote_option
(
    id         BIGINT AUTO_INCREMENT NOT NULL,
    created_at datetime              NULL,
    updated_at datetime              NULL,
    deleted_at datetime              NULL,
    post_id    BIGINT                NULL,
    name       VARCHAR(255)          NULL,
    count      INT                   NULL,
    CONSTRAINT pk_voteoption PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_identifier UNIQUE (identifier);

ALTER TABLE user
    ADD CONSTRAINT uc_user_nickname UNIQUE (nickname);