package com.ruoyi.system.task;

import com.ruoyi.system.service.IToRiskScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class ToRiskScoreRunner implements CommandLineRunner {

    @Autowired
    private IToRiskScoreService riskScoreService;

    @Override
    public void run(String... args) {
        new Thread(() -> {
            while (true) {
                try {
                    riskScoreService.processNextBatch();
                    Thread.sleep(1000); // 每批间隔2秒
                } catch (Exception e) {
                    e.printStackTrace();
                    try {
                        Thread.sleep(10000);
                    } catch (InterruptedException ignored) {
                    }
                }
            }
        }).start();
    }
}
