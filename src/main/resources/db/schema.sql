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
CREATE TABLE tokens
(
    token_id        INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id         INTEGER NOT NULL,
    token           VARCHAR(255) NOT NULL,
    creation_time   TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    expiration_time TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users (id)
);