CREATE TABLE comment
(
    created_at  DATETIME(6),
    id          BIGINT NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6),
    post_id     BIGINT,
    user_id     BIGINT,
    content     VARCHAR(255),
    PRIMARY KEY (id)
);

CREATE TABLE post
(
    closed_at   DATETIME(6),
    created_at  DATETIME(6),
    id          BIGINT NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6),
    user_id     BIGINT,
    content     VARCHAR(255),
    title       VARCHAR(255),
    status      ENUM ('COMPLETED', 'PROGRESS'),
    PRIMARY KEY (id)
);

CREATE TABLE user
(
    created_at  DATETIME(6),
    id          BIGINT NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6),
    email       VARCHAR(255),
    image_url   VARCHAR(1000),
    password    VARCHAR(255),
    provider    VARCHAR(255),
    provider_id VARCHAR(255),
    role        ENUM ('ROLE_ADMIN', 'ROLE_USER'),
    PRIMARY KEY (id)
);

CREATE TABLE vote
(
    created_at     DATETIME(6),
    id             BIGINT NOT NULL AUTO_INCREMENT,
    modified_at    DATETIME(6),
    post_id        BIGINT,
    vote_option_id BIGINT,
    PRIMARY KEY (id)
);

CREATE TABLE vote_option
(
    count       INTEGER,
    created_at  DATETIME(6),
    id          BIGINT NOT NULL AUTO_INCREMENT,
    modified_at DATETIME(6),
    post_id     BIGINT,
    name        VARCHAR(255),
    PRIMARY KEY (id)
);
CREATE TABLE signout
(
    signout_id BIGINT AUTO_INCREMENT NOT NULL,
    identifier VARCHAR(255)          NULL,
    reason     VARCHAR(255)          NULL,
    signout_at datetime              NULL,
    CONSTRAINT pk_signout PRIMARY KEY (signout_id)
);