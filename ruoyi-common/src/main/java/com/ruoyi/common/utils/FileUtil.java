package com.ruoyi.common.utils;


import java.io.File;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;

public class FileUtil {

    /**
     * 删除指定目录下 N 天前的 .sql 文件
     */
    public static void deleteFilesOlderThan(String directoryPath, int days) {
        File dir = new File(directoryPath);
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        long cutoffTime = LocalDate.now().minusDays(days)
                .atStartOfDay(ZoneId.systemDefault()).toInstant().toEpochMilli();

        File[] files = dir.listFiles((d, name) -> name.endsWith(".sql"));
        if (files == null) return;

        for (File file : files) {
            if (file.lastModified() < cutoffTime) {
                boolean deleted = file.delete();
                System.out.println("Deleted old backup: " + file.getName() + " -> " + deleted);
            }
        }
    }
}
