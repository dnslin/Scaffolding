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
    token_id        integer primary key,
    creation_time   timestamp    not null,
    expiration_time timestamp,
    token           varchar(255) not null,
    user_id         integer      not null,
    name            varchar(255) not null
);
CREATE TABLE site_config
(
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    siteName        TEXT NOT NULL,
    logoUrl         TEXT,
    faviconUrl      TEXT,
    siteDescription TEXT,
    copyright       TEXT,
    contactEmail    TEXT,
    announcement    TEXT -- 新增的公告字段
);

CREATE TABLE IF NOT EXISTS images
(
    id             INTEGER PRIMARY KEY AUTOINCREMENT,
    userId         INTEGER     NOT NULL, --用户id
    domain         TEXT        NOT NULL, -- 图片域名
    directory      TEXT        NOT NULL, -- 图片目录
    album          TEXT        NOT NULL, -- 图片相册
    createDate     DATE        NOT NULL, -- 创建时间
    modifyDate     DATE        NOT NULL, -- 修改时间
    fileName       TEXT        NOT NULL, -- 文件名
    encryptedKey   TEXT UNIQUE NOT NULL, -- 加密key 防止源地址泄露
    size           INTEGER     NOT NULL, -- 图片大小
    info           TEXT,                 -- 图片信息
    tags           TEXT,                 -- 图片标签
    visibility     INTEGER        NOT NULL, -- 是否私密图片 0:private 1:public ，private则需要密码访问
    category       TEXT,                 -- 分类字段
    accessPassword TEXT                  -- 访问密码字段
);

CREATE INDEX idx_userId ON images (userId);
CREATE INDEX idx_visibility ON images (visibility);
CREATE INDEX idx_category ON images (category);
CREATE INDEX idx_createDate ON images (createDate);
CREATE INDEX idx_modifyDate ON images (modifyDate);
