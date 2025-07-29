package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;
import com.ruoyi.system.service.IToRiskScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.time.ZoneId;
import java.util.*;

@Service
public class ToRiskScoreServiceImpl implements IToRiskScoreService {

    private static final int BATCH_SIZE = 10;
    private static final String POSITION_FILE = "risk_position.txt";

    @Autowired
    private ToVehicleRealTimMonitoringMapper toVehicleRealTimMonitoringMapper;

    @Override
    public void processNextBatch() {
        long lastId = readLastProcessedId();
        List<ToVehicleRealTimMonitoring> list = toVehicleRealTimMonitoringMapper.selectAfterId(lastId, BATCH_SIZE);

        if (list.isEmpty()) return;

        long maxId = lastId;

        for (ToVehicleRealTimMonitoring data : list) {
            double score = 0;

            // 1. 车型风险（5%）
            List<String> highTypes = Arrays.asList("栏板货车", "小型客车", "SUV/MPV");
            score += highTypes.contains(data.getVehicleType()) ? 5 : 0;

            // 2. 车膜风险（5%）
            //score += "深色膜".equals(data.getPlateColor()) ? 5 : 0;

            // 3. 品牌风险（5%）
            List<String> brands = Arrays.asList("金杯", "江铃", "五菱", "福特", "依维柯", "福田");
            score += data.getVehicleLogoAll() != null && brands.stream().anyMatch(b -> data.getVehicleLogoAll().contains(b)) ? 5 : 0;

            // 4. 车牌风险（20%）
            score += data.getPlate() != null && data.getPlate().startsWith("X") ? 20 : 0;

            // 5. 人脸风险（30%）
            score += data.getPlateLicense() != null && data.getPlateLicense().contains("案底") ? 30 : 0;

            // 6. 时间风险（10%）
            int hour = Optional.ofNullable(data.getCaptureTime())
                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getHour())
                    .orElse(12);
            score += (hour >= 0 && hour < 6) ? 10 : 0;

            // 7. 月份风险（10%）
            int month = Optional.ofNullable(data.getCaptureTime())
                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getMonthValue())
                    .orElse(1);
            score += (month >= 9 && month <= 11) ? 10 : 0;

            // 8. 地点风险（5%）
            score += data.getDirection() != null &&
                    (data.getDirection().contains("农村") || data.getDirection().contains("高速")) ? 5 : 0;

            toVehicleRealTimMonitoringMapper.updateLevel(data.getId(),  score);

            if (data.getId() > maxId) {
                maxId = data.getId();
            }
        }

        writeLastProcessedId(maxId);
    }

    private long readLastProcessedId() {
        try (BufferedReader reader = new BufferedReader(new FileReader(POSITION_FILE))) {
            String line = reader.readLine();
            return line != null ? Long.parseLong(line) : 0;
        } catch (IOException e) {
            return 0;
        }
    }

    private void writeLastProcessedId(long id) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(POSITION_FILE, false))) {
            writer.write(String.valueOf(id));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
