package com.ruoyi.system.task;

import com.ruoyi.system.service.impl.SysBackupServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DataBackupTask {

    @Autowired
    private SysBackupServiceImpl backupService;

    /**
     * 每 12 小时执行一次：凌晨 00:00 和 中午 12:00
     * Cron: 秒 分 时 日 月 周
     */

    //测试备份
    //@Scheduled(cron = "*/30 * * * * ?")  // 每 30 秒执行一次（测试用）
    @Scheduled(cron = "0 0 */12 * * ?")
    public void executeBackup() {
        backupService.backupTrafficDataTable();
    }
}