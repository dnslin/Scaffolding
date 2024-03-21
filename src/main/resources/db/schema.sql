-- 创建用户表
CREATE TABLE IF NOT EXISTS users
(
    id              INTEGER PRIMARY KEY AUTOINCREMENT,
    username        VARCHAR(255) UNIQUE NOT NULL,
    password        VARCHAR(255)        NOT NULL,
    enabled         BOOLEAN             NOT NULL,
    last_login_time DATETIME,
    last_login_ip   VARCHAR(255)
);

-- 创建登录日志表
CREATE TABLE IF NOT EXISTS login_logs
(
    id         INTEGER PRIMARY KEY AUTOINCREMENT,
    user_id    INTEGER,
    login_time DATETIME     NOT NULL,
    login_ip   VARCHAR(255) NOT NULL,
    status     VARCHAR(50)  NOT NULL, -- 例如 "成功", "失败"
    reason     VARCHAR(255),          -- 登录失败的原因，对于成功的登录，这可以是NULL
    FOREIGN KEY (user_id) REFERENCES users (id)
);

-- 创建OAuth客户端详情表
CREATE TABLE IF NOT EXISTS oauth_client_details
(
    client_id               VARCHAR(255) PRIMARY KEY,
    client_secret           VARCHAR(255) NOT NULL,
    resource_ids            VARCHAR(255),
    scope                   VARCHAR(255),
    authorized_grant_types  VARCHAR(255),
    web_server_redirect_uri VARCHAR(255),
    authorities             VARCHAR(255),
    access_token_validity   INTEGER,
    refresh_token_validity  INTEGER,
    additional_information  VARCHAR(4096),
    auto_approve             VARCHAR(255)
);

-- 创建OAuth授权码表
CREATE TABLE IF NOT EXISTS oauth_code
(
    code           VARCHAR(255),
    authentication BLOB
);

-- 创建OAuth访问令牌表
CREATE TABLE IF NOT EXISTS oauth_access_token
(
    token_id          VARCHAR(255),
    token             BLOB,
    authentication_id VARCHAR(255) PRIMARY KEY,
    username          VARCHAR(255),
    client_id         VARCHAR(255),
    authentication    BLOB,
    refresh_token     VARCHAR(255)
);

-- 创建OAuth刷新令牌表
CREATE TABLE IF NOT EXISTS oauth_refresh_token
(
    token_id       VARCHAR(255),
    token          BLOB,
    authentication BLOB
);
