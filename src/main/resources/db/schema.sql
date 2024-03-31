create table users
(
    id              integer
        primary key,
    enabled         boolean      not null,
    last_login_ip   varchar(255),
    last_login_time timestamp,
    password        varchar(255) not null,
    username        varchar(255) not null
        unique,
    avatar          varchar(255) not null,
    email           varchar(255) not null,
    nickname        varchar(255) not null,
    salt            varchar(255) not null
);

CREATE TABLE roles
(
    id          INTEGER PRIMARY KEY,
    name        VARCHAR(255) NOT NULL,
    description VARCHAR(255)
);
CREATE TABLE "tokens"
(
    token_id        integer
        primary key,
    creation_time   timestamp    not null,
    expiration_time timestamp,
    token           varchar(255) not null,
    user_id         integer      not null,
    name            varchar(255) not null
);

