CREATE TABLE comment
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    modified_at datetime NULL,
    user_id     BIGINT NULL,
    post_id     BIGINT NULL,
    content     VARCHAR(255) NULL,
    CONSTRAINT pk_comment PRIMARY KEY (id)
);

CREATE TABLE post
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    modified_at datetime NULL,
    user_id     BIGINT NULL,
    title       VARCHAR(255) NULL,
    content     VARCHAR(255) NULL,
    status      VARCHAR(255) NULL,
    closed_at   datetime NULL,
    CONSTRAINT pk_post PRIMARY KEY (id)
);

CREATE TABLE signout
(
    signout_id BIGINT AUTO_INCREMENT NOT NULL,
    identifier VARCHAR(255) NULL,
    reason     VARCHAR(255) NULL,
    signout_at datetime NULL,
    CONSTRAINT pk_signout PRIMARY KEY (signout_id)
);

CREATE TABLE user
(
    id            BIGINT AUTO_INCREMENT NOT NULL,
    created_at    datetime NULL,
    modified_at   datetime NULL,
    email         VARCHAR(255) NULL,
    nickname      VARCHAR(20) NULL,
    information   VARCHAR(100) NULL,
    identifier    VARCHAR(255) NOT NULL,
    provider_info VARCHAR(255) NOT NULL,
    image_url     VARCHAR(1000) NULL,
    `role`        VARCHAR(255) NOT NULL,
    provider      VARCHAR(255) NULL,
    provider_id   VARCHAR(255) NULL,
    CONSTRAINT pk_user PRIMARY KEY (id)
);

CREATE TABLE vote
(
    id             BIGINT AUTO_INCREMENT NOT NULL,
    created_at     datetime NULL,
    modified_at    datetime NULL,
    post_id        BIGINT NULL,
    vote_option_id BIGINT NULL,
    CONSTRAINT pk_vote PRIMARY KEY (id)
);

CREATE TABLE vote_option
(
    id          BIGINT AUTO_INCREMENT NOT NULL,
    created_at  datetime NULL,
    modified_at datetime NULL,
    post_id     BIGINT NULL,
    name        VARCHAR(255) NULL,
    count       INT NULL,
    CONSTRAINT pk_voteoption PRIMARY KEY (id)
);

ALTER TABLE user
    ADD CONSTRAINT uc_user_information UNIQUE (information);

ALTER TABLE user
    ADD CONSTRAINT uc_user_nickname UNIQUE (nickname);