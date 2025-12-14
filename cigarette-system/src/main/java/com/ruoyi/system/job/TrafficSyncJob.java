package com.ruoyi.system.job;

import com.ruoyi.system.service.TrafficDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class TrafficSyncJob {

    @Autowired
    private TrafficDataSyncService syncService;

    @Scheduled(fixedDelay = 1000)
    public void run() {
        syncService.sync();
    }
}