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
CREATE TABLE IF NOT EXISTS SiteConfig
(
    id               INTEGER PRIMARY KEY AUTOINCREMENT,
    site_name        TEXT NOT NULL,
    site_url         TEXT NOT NULL,
    favicon_url      TEXT NOT NULL,
    site_description TEXT NOT NULL,
    site_keywords    TEXT NOT NULL,
    contact_email    TEXT NOT NULL,
    contact_qq       TEXT NOT NULL
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
    visibility     INTEGER     NOT NULL, -- 是否私密图片 0:private 1:public ，private则需要密码访问
    category       TEXT,                 -- 分类字段
    accessPassword TEXT                  -- 访问密码字段
);

CREATE INDEX idx_userId ON images (userId);
CREATE INDEX idx_visibility ON images (visibility);
CREATE INDEX idx_category ON images (category);
CREATE INDEX idx_createDate ON images (createDate);
CREATE INDEX idx_modifyDate ON images (modifyDate);



CREATE TABLE IF NOT EXISTS image_config
(
    id                    INTEGER PRIMARY KEY AUTOINCREMENT,
    allowed_extensions    TEXT    NOT NULL, -- 允许的扩展名，以逗号分隔的字符串
    naming_strategy       TEXT    NOT NULL, -- 图片命名方式
    allow_format_conversion BOOLEAN NOT NULL, -- 是否允许转换格式
    max_file_size         INTEGER NOT NULL, -- 图片文件最大尺寸（字节）
    min_file_size         INTEGER NOT NULL, -- 图片文件最小尺寸（字节）
    max_upload_count      INTEGER NOT NULL, -- 一次最多上传多少张图片
    max_upload_width      INTEGER NOT NULL, -- 图片最大上传宽度（像素）
    min_upload_width      INTEGER NOT NULL, -- 图片最小上传宽度（像素）
    max_upload_height     INTEGER NOT NULL, -- 图片最大上传高度（像素）
    min_upload_height     INTEGER NOT NULL, -- 图片最小上传高度（像素）
    enable_compression    BOOLEAN NOT NULL  -- 是否开启压缩
);
CREATE TABLE IF NOT EXISTS system_configurations
(
    id                      INTEGER PRIMARY KEY AUTOINCREMENT,
    thumbnail_enabled       INTEGER DEFAULT 0 NOT NULL,  -- 缩略图开启
    blacklist_enabled       TEXT,  -- 黑名单
    login_upload_enabled    INTEGER DEFAULT 0 NOT NULL,  -- 开启登录上传
    api_upload_enabled      INTEGER DEFAULT 0 NOT NULL,  -- 开启API上传
    encryption_enabled      INTEGER DEFAULT 0 NOT NULL,  -- 开启加密
    trash_bin_enabled       INTEGER DEFAULT 0 NOT NULL,  -- 开启回收站
    delete_link_enabled     INTEGER DEFAULT 0 NOT NULL,  -- 开启删除链接
    upload_log_enabled      INTEGER DEFAULT 0 NOT NULL,  -- 开启上传日志
    login_log_enabled       INTEGER DEFAULT 0 NOT NULL,  -- 开启登录日志
    timezone                TEXT    NOT NULL,            -- 时区配置
    guest_upload_limit      TEXT    NOT NULL,            -- 游客上传限制（格式为IP:张数）
    image_compression_enabled INTEGER DEFAULT 0 NOT NULL,-- 开启图片压缩
    image_conversion_enabled INTEGER DEFAULT 0 NOT NULL, -- 开启图片转换
    trash_bin_days          INTEGER DEFAULT 0 NOT NULL   -- 回收站存在天数
);
