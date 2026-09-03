package com.ruoyi.system.job;

import com.ruoyi.system.service.TrafficDataSyncService;
import com.ruoyi.system.service.impl.SmsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

// 该文件用于批量处理定时任务
@Component
public class TrafficSyncJob {

    @Autowired
    private TrafficDataSyncService syncService;
    @Autowired
    private SmsService smsService;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        try {
            syncService.sync();
        } catch (Exception ignored) {
        }
    }

    //    （用于测试终端设备数据接收格式）
//    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
//    public void pullMessage() {
//        try {
//            smsService.sendSms("18313946676","【云南省烟草公司曲靖市公司】车牌：云D289ZD（SUV/MPV-五菱）于07:23:00经过陆良板桥收费站，风险因子：车辆属性、驾驶时间，ID：1245314。请拦截！");
//            smsService.sendSms("18313946676","【云南省烟草公司曲靖市公司】车牌：云D289ZD（SUV/MPV-五菱）于07:23:00经过陆良板桥收费站，风险因子：车辆属性、驾驶时间，ID：1245314。请拦截！");
//        } catch (Exception ignored) {
//        }
//    }

    //    该定时任务用于向第三方短信平台拉取用户回复的短信
    //    该定时任务用于处理用户回复的短信中的判别具体违法车辆的数据
    //    设置定时任务为每天的凌晨2点运行一次
    @Scheduled(cron = "0 0 2 * * ?")
//    @Scheduled(initialDelay = 10000, fixedDelay = 5000)
    public void handleMessage() {
        try {
            smsService.pullUpstreamMessages();
//            smsService.handleMessage();
        } catch (Exception ignored) {
        }
    }
}