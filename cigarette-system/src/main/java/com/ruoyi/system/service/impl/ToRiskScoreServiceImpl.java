//package com.ruoyi.system.service.impl;
//
//import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
//import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;
//import com.ruoyi.system.service.IToRiskScoreService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.time.ZoneId;
//import java.util.*;
//
//@Service
//public class ToRiskScoreServiceImpl implements IToRiskScoreService {
//
//    private static final int BATCH_SIZE = 100;
//    private static final String POSITION_FILE = "risk_position.txt";
//
//    @Autowired
//    private ToVehicleRealTimMonitoringMapper toVehicleRealTimMonitoringMapper;
//
//    @Override
//    public void processNextBatch() {
//        long lastId = readLastProcessedId();
//        List<ToVehicleRealTimMonitoring> list = toVehicleRealTimMonitoringMapper.selectAfterId(lastId, BATCH_SIZE);
//
//        if (list.isEmpty()) return;
//
//        long maxId = lastId;
//
//        for (ToVehicleRealTimMonitoring data : list) {
//            double score = 0;
//
//            // 1. 车型风险（5%）
//            List<String> highTypes = Arrays.asList("栏板货车", "小型客车", "SUV/MPV");
//            score += highTypes.contains(data.getVehicleType()) ? 5 : 0;
//
//            // 2. 车膜风险（5%）
//            //score += "深色膜".equals(data.getPlateColor()) ? 5 : 0;
//
//            // 3. 品牌风险（5%）
//            List<String> brands = Arrays.asList("金杯", "江铃", "五菱", "福特", "依维柯", "福田");
//            score += data.getVehicleLogoAll() != null && brands.stream().anyMatch(b -> data.getVehicleLogoAll().contains(b)) ? 30 : 0;
//            //测试1392
//            if (data.getVehicleLogoAll() != null) {
//                String[] parts = data.getVehicleLogoAll().split("-");
//                System.out.println("品牌:" + data.getVehicleLogo() + "\n" + "型号:" + parts[1]);
//                List<String> brandList = Arrays.asList("全顺", "聚星", "图雅诺", "SUV", "栅栏货车");
//                score += brandList.contains(parts[1]) ? 30 : 0;
//            } else {
//                System.out.println("空");
//            }
//
//            //
//            // 4. 车牌风险（20%）
//            score += data.getPlate() != null && data.getPlate().startsWith("X") ? 20 : 0;
//
//            // 5. 人脸风险（30%）
//            score += data.getPlateLicense() != null && data.getPlateLicense().contains("案底") ? 30 : 0;
//
//            // 6. 时间风险（10%）
//            int hour = Optional.ofNullable(data.getCaptureTime())
//                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getHour())
//                    .orElse(12);
//            System.out.println("\n时长" + hour);
//            score += (hour >= 0 && hour < 6) ? 10 : 0;
//
//            // 7. 月份风险（10%）
//            int month = Optional.ofNullable(data.getCaptureTime())
//                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getMonthValue())
//                    .orElse(1);
//            score += (month >= 9 && month <= 11) ? 10 : 0;
//            System.out.println("\n月份" + month);
//            // 8. 地点风险（5%）
//            score += data.getDirection() != null &&
//                    (data.getDirection().contains("农村") || data.getDirection().contains("高速")) ? 5 : 0;
//
//            toVehicleRealTimMonitoringMapper.updateLevel(data.getId(), score);
//
//            if (data.getId() > maxId) {
//                maxId = data.getId();
//            }
//        }
//
//        writeLastProcessedId(maxId);
//    }
//
//    private long readLastProcessedId() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(POSITION_FILE))) {
//            String line = reader.readLine();
//            return line != null ? Long.parseLong(line) : 0;
//        } catch (IOException e) {
//            return 0;
//        }
//    }
//
//    private void writeLastProcessedId(long id) {
//        try (BufferedWriter writer = new BufferedWriter(new FileWriter(POSITION_FILE, false))) {
//            writer.write(String.valueOf(id));
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//    }
//}
package com.ruoyi.system.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
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

    private static final int BATCH_SIZE = 100;
    private static final String POSITION_FILE = "risk_position.txt";
    private static final String FACTOR_FILE = "config/risk_factor.json";

    @Autowired
    private ToVehicleRealTimMonitoringMapper toVehicleRealTimMonitoringMapper;

    @Override
    public void processNextBatch() {
        long lastId = readLastProcessedId();
        List<ToVehicleRealTimMonitoring> list = toVehicleRealTimMonitoringMapper.selectAfterId(lastId, BATCH_SIZE);

        if (list.isEmpty()) return;

        long maxId = lastId;
        Map<String, Integer> factor = loadRiskFactors();

        for (ToVehicleRealTimMonitoring data : list) {
            double score = 0;

            // 1. 车型风险
            List<String> highTypes = Arrays.asList("栏板货车", "小型客车", "SUV/MPV");
            score += highTypes.contains(data.getVehicleType()) ? factor.getOrDefault("car_type_factor", 5) : 0;

            // 2. 车膜风险（示例注释掉了，可启用）
            // score += "深色膜".equals(data.getPlateColor()) ? factor.getOrDefault("car_film_factor", 5) : 0;

            // 3. 品牌风险
            List<String> brandMatch = Arrays.asList("金杯", "江铃", "五菱", "福特", "依维柯", "福田");
            if (data.getVehicleLogoAll() != null && brandMatch.stream().anyMatch(b -> data.getVehicleLogoAll().contains(b))) {
                score += factor.getOrDefault("brand_factor", 5);
            }

            if (data.getVehicleLogoAll() != null) {
                String[] parts = data.getVehicleLogoAll().split("-");
                if (parts.length > 1) {
                    List<String> modelMatch = Arrays.asList("全顺", "聚星", "图雅诺", "SUV", "栅栏货车");
                    if (modelMatch.contains(parts[1])) {
                        score += factor.getOrDefault("brand_factor", 5);
                    }
                }
            }

            // 4. 车牌风险
            if (data.getPlate() != null && data.getPlate().startsWith("X")) {
                score += factor.getOrDefault("LPN_factor", 20);
            }

            // 5. 人脸风险
            if (data.getPlateLicense() != null && data.getPlateLicense().contains("案底")) {
                score += factor.getOrDefault("face_factor", 30);
            }

            // 6. 时间风险
            int hour = Optional.ofNullable(data.getCaptureTime())
                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getHour())
                    .orElse(12);
            if (hour >= 0 && hour < 6) {
                score += factor.getOrDefault("time_factor", 10);
            }

            // 7. 月份风险
            int month = Optional.ofNullable(data.getCaptureTime())
                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getMonthValue())
                    .orElse(1);
            if (month >= 9 && month <= 11) {
                score += factor.getOrDefault("month_factor", 10);
            }

            // 8. 地点风险
            if (data.getDirection() != null &&
                    (data.getDirection().contains("农村") || data.getDirection().contains("高速"))) {
                score += factor.getOrDefault("location_factor", 5);
            }

            // 更新分数
            toVehicleRealTimMonitoringMapper.updateLevel(data.getId(), score);

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

    private Map<String, Integer> loadRiskFactors() {
        File file = new File(FACTOR_FILE);
        if (!file.exists()) {
            return getDefaultFactors();
        }

        try (Reader reader = new FileReader(file)) {
            return new ObjectMapper().readValue(reader, Map.class);
        } catch (IOException e) {
            e.printStackTrace();
            return getDefaultFactors();
        }
    }

    private Map<String, Integer> getDefaultFactors() {
        Map<String, Integer> defaults = new HashMap<>();
        defaults.put("car_type_factor", 5);
        defaults.put("car_film_factor", 5);
        defaults.put("brand_factor", 5);
        defaults.put("LPN_factor", 20);
        defaults.put("face_factor", 30);
        defaults.put("time_factor", 10);
        defaults.put("month_factor", 10);
        defaults.put("location_factor", 5);
        return defaults;
    }
}
