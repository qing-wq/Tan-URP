package ink.whi.core.util;

import java.io.File;

/**
 * @author: qing
 * @Date: 2023/6/6
 */
public class FileUtil {

    /**
     * 获取文件大小
     * @param path
     * @return
     */
    public static Long getFileSize(String path) {
        File file = new File(path);
        return file.length();
    }
}
