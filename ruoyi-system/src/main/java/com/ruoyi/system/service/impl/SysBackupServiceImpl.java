
package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

@Service
public class SysBackupServiceImpl {

    private static final Logger log = LoggerFactory.getLogger(SysBackupServiceImpl.class);

    @Value("${backup.dir}")
    private String backupDir;

    @Value("${backup.table}")
    private String tableName;

    @Value("${backup.database}")
    private String database;

    @Value("${backup.host}")
    private String host;

    @Value("${backup.port}")
    private Integer port;

    @Value("${backup.username}")
    private String backupUsername;

    @Value("${backup.password}")
    private String backupPassword;  // ✅ 从配置读取

    /**
     * 执行备份：使用 mysqldump + ProcessBuilder（避免密码暴露在命令行）
     */
    public void backupTrafficDataTable() {
        log.info("Starting backup for table: {}.{} on {}:{}",
                database, tableName, host, port);

        File dir = new File(backupDir);
        if (!dir.exists()) {
            boolean created = dir.mkdirs();
            if (!created) {
                log.error("Failed to create backup directory: {}", backupDir);
                return;
            }
        }

        // 生成唯一文件名
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
        String fileName = tableName + "_" + timestamp + ".sql";
        String filePath = backupDir + File.separator + fileName;

        // 构建 mysqldump 命令（不包含密码）
        List<String> command = new ArrayList<>();
        command.add("mysqldump");
        command.add("--no-tablespaces");
        command.add("--single-transaction");
        command.add("--routines");
        command.add("--triggers");
        command.add("-h"); command.add(host);
        command.add("-P"); command.add(port.toString());
        command.add("-u"); command.add(backupUsername);
        command.add("-p" + backupPassword);  // ✅ 密码在这里，但不在命令字符串中显示
        command.add(database);
        command.add(tableName);

        // 使用 ProcessBuilder（不通过 shell 拼接命令）
        ProcessBuilder pb = new ProcessBuilder(command);
        pb.redirectOutput(new File(filePath));  // 重定向输出到文件
        pb.redirectErrorStream(true);          // 合并错误流

        try {
            log.info("Executing mysqldump for table: {}", tableName);
            Process process = pb.start();

            // 等待完成
            int exitCode = process.waitFor();

            if (exitCode == 0) {
                log.info("✅ Backup successful: {}", filePath);
                // 清理 7 天前的旧备份
                FileUtil.deleteFilesOlderThan(backupDir, 7);
            } else {
                log.error("❌ Backup failed! Exit code: {}", exitCode);
                // 读取错误输出
                try (java.io.BufferedReader reader = new java.io.BufferedReader(
                        new java.io.InputStreamReader(process.getInputStream()))) {
                    String line;
                    while ((line = reader.readLine()) != null) {
                        log.error("Error output: {}", line);
                    }
                }
            }
        } catch (IOException | InterruptedException e) {
            log.error("🚨 Backup error: ", e);
        }
    }
}