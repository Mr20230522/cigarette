package com.ruoyi.quartz.task;

import com.ruoyi.system.service.ITobAlertTaskService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 预警任务超时处理定时任务
 * 每60秒执行一次：
 * 1. 任务创建超过2分钟无人接受 → 推送管理员
 * 2. 任务创建超过2小时仍status=0 → 自动关闭（status=3）
 */
@Component("alertTaskTimeoutJob")
public class AlertTaskTimeoutJob {

    private static final Logger log = LoggerFactory.getLogger(AlertTaskTimeoutJob.class);

    @Autowired
    private ITobAlertTaskService tobAlertTaskService;

    /**
     * 处理超时任务
     */
    public void processTimeout() {
        log.info("开始处理超时预警任务...");
        try {
            tobAlertTaskService.processTimeoutTasks();
        } catch (Exception e) {
            log.error("处理超时预警任务失败", e);
        }
        log.info("超时预警任务处理完成");
    }
}
