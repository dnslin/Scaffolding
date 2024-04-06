package in.dnsl.constant;

import lombok.Data;

@Data
public class FilePath {

    // 图片临时存储目录 Windows
    public static final String IMAGE_TEMP_PATH = "D:/temp/image/";

    // 图片临时存储目录 Linux
    public static final String IMAGE_TEMP_PATH_LINUX = "/app/data/temp/image/";
}
