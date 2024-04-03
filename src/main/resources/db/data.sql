INSERT INTO site_config (site_name, site_url, favicon_url, site_description, site_keywords, contact_email, contact_qq)
VALUES
    ('PicManger', 'https://pic.dnsl.in/path/to/logo.png', 'https://pic.dnsl.in/path/to/favicon.ico', '专注于图片分享和管理的平台。', '图片分享,图片管理,在线相册', 'i@dnsl.in', '欢迎访问我们的图片管理网站，这里是最新公告：我们刚刚更新了平台功能，希望大家喜欢！');



INSERT INTO images (
    allowed_extensions,
    naming_strategy,
    allow_format_conversion,
    max_file_size,
    min_file_size,
    max_upload_count,
    max_upload_width,
    min_upload_width,
    max_upload_height,
    min_upload_height,
    enable_compression
) VALUES (
             'jpg,png,gif', -- 允许jpg, png, gif格式的图片
             'timestamp',   -- 使用时间戳命名方式
             1,             -- 允许格式转换
             5242880,       -- 最大文件大小5MB
             10240,         -- 最小文件大小10KB
             20,            -- 一次最多上传20张图片
             1920,          -- 最大上传宽度1920像素
             100,           -- 最小上传宽度100像素
             1080,          -- 最大上传高度1080像素
             100,           -- 最小上传高度100像素
             1              -- 开启压缩
         );