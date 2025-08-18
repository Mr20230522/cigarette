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
//package com.ruoyi.system.service.impl;
//
//import com.fasterxml.jackson.databind.ObjectMapper;
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
//    private static final String FACTOR_FILE = "config/risk_factor.json";
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
//        Map<String, Integer> factor = loadRiskFactors();
//
//        for (ToVehicleRealTimMonitoring data : list) {
//            double score = 0;
//
//            // 1. 车型风险
//            List<String> highTypes = Arrays.asList("栏板货车", "小型客车", "SUV/MPV");
//            score += highTypes.contains(data.getVehicleType()) ? factor.getOrDefault("car_type_factor", 5) : 0;
//
//            // 2. 车膜风险（示例注释掉了，可启用）
//            // score += "深色膜".equals(data.getPlateColor()) ? factor.getOrDefault("car_film_factor", 5) : 0;
//
//            // 3. 品牌风险
//            List<String> brandMatch = Arrays.asList("金杯", "江铃", "五菱", "福特", "依维柯", "福田");
//            if (data.getVehicleLogoAll() != null && brandMatch.stream().anyMatch(b -> data.getVehicleLogoAll().contains(b))) {
//                score += factor.getOrDefault("brand_factor", 5);
//            }
//
//            if (data.getVehicleLogoAll() != null) {
//                String[] parts = data.getVehicleLogoAll().split("-");
//                if (parts.length > 1) {
//                    List<String> modelMatch = Arrays.asList("全顺", "聚星", "图雅诺", "SUV", "栅栏货车");
//                    if (modelMatch.contains(parts[1])) {
//                        score += factor.getOrDefault("brand_factor", 5);
//                    }
//                }
//            }
//
//            // 4. 车牌风险
//            if (data.getPlate() != null && data.getPlate().startsWith("X")) {
//                score += factor.getOrDefault("LPN_factor", 20);
//            }
//
//            // 5. 人脸风险
//            if (data.getPlateLicense() != null && data.getPlateLicense().contains("案底")) {
//                score += factor.getOrDefault("face_factor", 30);
//            }
//
//            // 6. 时间风险
//            int hour = Optional.ofNullable(data.getCaptureTime())
//                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getHour())
//                    .orElse(12);
//            if (hour >= 0 && hour < 6) {
//                score += factor.getOrDefault("time_factor", 10);
//            }
//
//            // 7. 月份风险
//            int month = Optional.ofNullable(data.getCaptureTime())
//                    .map(t -> t.toInstant().atZone(ZoneId.systemDefault()).getMonthValue())
//                    .orElse(1);
//            if (month >= 9 && month <= 11) {
//                score += factor.getOrDefault("month_factor", 10);
//            }
//
//            // 8. 地点风险
//            if (data.getDirection() != null &&
//                    (data.getDirection().contains("农村") || data.getDirection().contains("高速"))) {
//                score += factor.getOrDefault("location_factor", 5);
//            }
//
//            // 更新分数
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
//
//    private Map<String, Integer> loadRiskFactors() {
//        File file = new File(FACTOR_FILE);
//        if (!file.exists()) {
//            return getDefaultFactors();
//        }
//
//        try (Reader reader = new FileReader(file)) {
//            return new ObjectMapper().readValue(reader, Map.class);
//        } catch (IOException e) {
//            e.printStackTrace();
//            return getDefaultFactors();
//        }
//    }
//
//    private Map<String, Integer> getDefaultFactors() {
//        Map<String, Integer> defaults = new HashMap<>();
//        defaults.put("car_type_factor", 5);
//        defaults.put("car_film_factor", 5);
//        defaults.put("brand_factor", 5);
//        defaults.put("LPN_factor", 20);
//        defaults.put("face_factor", 30);
//        defaults.put("time_factor", 10);
//        defaults.put("month_factor", 10);
//        defaults.put("location_factor", 5);
//        return defaults;
//    }
//}
//第二版本

//package com.ruoyi.system.service.impl;
//
//import com.ruoyi.system.domain.ToFactorConfig;
//import com.ruoyi.system.domain.ToFactorTrigger;
//import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
//import com.ruoyi.system.mapper.ToFactorConfigMapper;
//import com.ruoyi.system.mapper.ToFactorTriggerMapper;
//import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;
//import com.ruoyi.system.service.IToRiskScoreService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.time.ZoneId;
//import java.util.*;
//import java.util.stream.Collectors;
//
//@Service
//public class ToRiskScoreServiceImpl implements IToRiskScoreService {
//
//    private static final int BATCH_SIZE = 5;
//    private static final String POSITION_FILE = "risk_position.txt";
//
//    @Autowired
//    private ToVehicleRealTimMonitoringMapper vehicleMapper;
//
//    @Autowired
//    private ToFactorConfigMapper factorConfigMapper;
//
//    @Autowired
//    private ToFactorTriggerMapper factorTriggerMapper;
//
//    @Override
//    public void processNextBatch() {
//        long lastId = readLastProcessedId();
//        List<ToVehicleRealTimMonitoring> dataList = vehicleMapper.selectAfterId(lastId, BATCH_SIZE);
//        if (dataList.isEmpty()) return;
//        ToFactorConfig filter1 = null;
//        ToFactorTrigger filter2 = null;
//        // 读取所有因子配置
//        List<ToFactorConfig> factors = factorConfigMapper.selectFactorConfigList(filter1);
//        System.out.println("条件"+factors);
//        // 读取所有因子触发条件
//        List<ToFactorTrigger> triggers = factorTriggerMapper.selectFactorTriggerList(filter2);
//        System.out.println("触发条件"+triggers);
//        // 按因子ID分组触发条件，方便快速查找
//        Map<Long, List<String>> factorTriggersMap = triggers.stream()
//                .collect(Collectors.groupingBy(ToFactorTrigger::getFactorId,
//                        Collectors.mapping(ToFactorTrigger::getTriggerValue, Collectors.toList())));
//
//        long maxId = lastId;
//
//        for (ToVehicleRealTimMonitoring data : dataList) {
//            double score = 0;
//
//            for (ToFactorConfig factor : factors) {
//                List<String> triggerValues = factorTriggersMap.getOrDefault(factor.getId(), Collections.emptyList());
//
//                if (isTriggered(data, triggerValues, factor.getFactorName())) {
//                    score += factor.getFactorValue() != null ? factor.getFactorValue() : 0;
//                }
//            }
//
//            // 更新数据库风险分数
//            vehicleMapper.updateLevel(data.getId(), score);
//
//            if (data.getId() > maxId) {
//                maxId = data.getId();
//            }
//        }
//
//        writeLastProcessedId(maxId);
//    }
//
//    /**
//     * 判断给定数据是否触发该因子
//     *
//     * @param data          车辆数据实体
//     * @param triggerValues 因子的触发值列表
//     * @param factorName    因子名称，用于字段匹配判断
//     * @return true-触发，false-未触发
//     */
//    private boolean isTriggered(ToVehicleRealTimMonitoring data, List<String> triggerValues, String factorName) {
//        if (triggerValues == null || triggerValues.isEmpty()) {
//            return false;
//        }
//
//        switch (factorName) {
//            case "车型风险":
//                // 直接判断车型是否包含在触发值列表中
//                return triggerValues.contains(data.getVehicleType());
//
//            case "品牌风险":
//                if (data.getVehicleLogoAll() == null) return false;
//                // 判断车辆品牌名是否包含触发关键词之一
//                for (String val : triggerValues) {
//                    if (data.getVehicleLogoAll().contains(val)) return true;
//                }
//                return false;
//
//            case "车牌风险":
//                if (data.getPlate() == null) return false;
//                for (String val : triggerValues) {
//                    if (data.getPlate().startsWith(val)) return true;
//                }
//                return false;
//
//            case "人脸风险":
//                if (data.getPlateLicense() == null) return false;
//                for (String val : triggerValues) {
//                    if (data.getPlateLicense().contains(val)) return true;
//                }
//                return false;
//
//            case "时间风险":
//                if (data.getCaptureTime() == null) return false;
//                int hour = data.getCaptureTime().toInstant().atZone(ZoneId.systemDefault()).getHour();
//                for (String val : triggerValues) {
//                    // 假设触发值格式是“开始小时-结束小时”，如 "0-6"
//                    if (val.contains("-")) {
//                        String[] parts = val.split("-");
//                        int start = Integer.parseInt(parts[0].trim());
//                        int end = Integer.parseInt(parts[1].trim());
//                        if (hour >= start && hour < end) return true;
//                    }
//                }
//                return false;
//
//            case "月份风险":
//                if (data.getCaptureTime() == null) return false;
//                int month = data.getCaptureTime().toInstant().atZone(ZoneId.systemDefault()).getMonthValue();
//                for (String val : triggerValues) {
//                    // 触发值格式同时间风险
//                    if (val.contains("-")) {
//                        String[] parts = val.split("-");
//                        int start = Integer.parseInt(parts[0].trim());
//                        int end = Integer.parseInt(parts[1].trim());
//                        if (month >= start && month <= end) return true;
//                    }
//                }
//                return false;
//
//            case "地点风险":
//                if (data.getDirection() == null) return false;
//                for (String val : triggerValues) {
//                    if (data.getDirection().contains(val)) return true;
//                }
//                return false;
//
//            default:
//                return false;
//        }
//    }
//
//    private long readLastProcessedId() {
//        try (BufferedReader reader = new BufferedReader(new FileReader(POSITION_FILE))) {
//            String line = reader.readLine();
//            return line != null ? Long.parseLong(line) : 0L;
//        } catch (IOException e) {
//            return 0L;
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
// 第三版本
//package com.ruoyi.system.service.impl;
//
//import com.ruoyi.system.domain.ToFactorConfig;
//import com.ruoyi.system.domain.ToFactorTrigger;
//import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
//import com.ruoyi.system.mapper.ToFactorConfigMapper;
//import com.ruoyi.system.mapper.ToFactorTriggerMapper;
//import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;
//import com.ruoyi.system.service.IToRiskScoreService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//
//import java.io.*;
//import java.nio.charset.StandardCharsets;
//import java.nio.file.*;
//import java.time.ZoneId;
//import java.time.ZonedDateTime;
//import java.util.*;
//
//import java.util.stream.Collectors;
//
///**
// * 改进版风险分数计算服务（单文件实现）
// * - 更健壮的触发值解析与错误处理
// * - 使用枚举映射因子类型，减少对裸字符串的耦合
// * - 更安全的游标文件读写
// * - 保持与原有 Mapper / Domain API 的兼容
// */
//@Service
//public class ToRiskScoreServiceImpl implements IToRiskScoreService {
//
//    private static final int BATCH_SIZE = 50;
//    private static final String POSITION_FILE = "risk_position.txt";
//    // 明确默认时区（如需改为服务器默认，可改为 ZoneId.systemDefault()）
//    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();
//
//    @Autowired
//    private ToVehicleRealTimMonitoringMapper vehicleMapper;
//
//    @Autowired
//    private ToFactorConfigMapper factorConfigMapper;
//
//    @Autowired
//    private ToFactorTriggerMapper factorTriggerMapper;
//
//    // 用枚举把因子名称和逻辑绑定，便于维护。仍然支持通过 factorName 字符串解析。
//    private enum FactorType {
//        VEHICLE_TYPE("车型风险"),
//        BRAND("品牌风险"),
//        PLATE("车牌风险"),
//        FACE("人脸风险"),
//        TIME("时间风险"),
//        MONTH("月份风险"),
//        LOCATION("地点风险"),
//        Sub_BRAND("子品牌风险"),
//        UNKNOWN("UNKNOWN");
//
//        private final String displayName;
//
//        FactorType(String displayName) {
//            this.displayName = displayName;
//        }
//
//        public static FactorType fromName(String name) {
//            if (name == null) return UNKNOWN;
//            String trimmed = name.trim();
//            for (FactorType ft : values()) {
//                if (ft.displayName.equalsIgnoreCase(trimmed)) {
//                    return ft;
//                }
//            }
//            // 兼容模糊匹配（若业务中可能出现微小差别）
//            if (trimmed.contains("car_type_factor")) return VEHICLE_TYPE;
//            if (trimmed.contains("brand_factor")) return BRAND;
//            if (trimmed.contains("sub_brand_factor")) return Sub_BRAND;
//            if (trimmed.contains("LPN_factor")) return PLATE;
//            if (trimmed.contains("face_factor")) return FACE;
//            if (trimmed.contains("time_factor") && !trimmed.contains("月")) return TIME;
//            if (trimmed.contains("month_factor")) return MONTH;
//            if (trimmed.contains("location_factor") || trimmed.contains("方向")) return LOCATION;
//            return UNKNOWN;
//        }
//    }
//
//    @Override
//    public void processNextBatch() {
//        long lastId = readLastProcessedId();
//        List<ToVehicleRealTimMonitoring> dataList = vehicleMapper.selectAfterId(lastId, BATCH_SIZE);
//        if (dataList == null || dataList.isEmpty()) {
//            return;
//        }
//
//        // 读取所有因子配置与触发条件
//        List<ToFactorConfig> factors = factorConfigMapper.selectFactorConfigList(null);
//        if (factors == null) factors = Collections.emptyList();
//
//        List<ToFactorTrigger> triggers = factorTriggerMapper.selectFactorTriggerList(null);
//        if (triggers == null) triggers = Collections.emptyList();
//
//        // 将触发条件按因子ID分组为字符串列表（原始值）
//        Map<Long, List<String>> factorTriggersMap = triggers.stream()
//                .filter(Objects::nonNull)
//                .collect(Collectors.groupingBy(ToFactorTrigger::getFactorId,
//                        Collectors.mapping(ToFactorTrigger::getTriggerValue, Collectors.toList())));
//        //System.out.println("触发条件映射: " + factorTriggersMap);
//        long maxId = lastId;
//
//        // 预解析每个因子对应的 FactorType 与 triggerValues 以减少循环内重复解析
//        class FactorMeta {
//            final ToFactorConfig config;
//            final FactorType type;
//            final List<String> triggerValues;
//
//            FactorMeta(ToFactorConfig config, FactorType type, List<String> triggerValues) {
//                this.config = config;
//                this.type = type;
//                this.triggerValues = triggerValues;
//            }
//        }
//
//        List<FactorMeta> factorMetas = new ArrayList<>();
//        for (ToFactorConfig factor : factors) {
//            if (factor == null) continue;
//            FactorType type = FactorType.fromName(factor.getFactorName());
//            List<String> tv = factorTriggersMap.getOrDefault(factor.getId(), Collections.emptyList());
//            // 去掉 null / 空串 并 trim
//            List<String> cleaned = tv.stream()
//                    .filter(Objects::nonNull)
//                    .map(String::trim)
//                    .filter(s -> !s.isEmpty())
//                    .collect(Collectors.toList());
//            factorMetas.add(new FactorMeta(factor, type, cleaned));
//        }
//
//        for (ToVehicleRealTimMonitoring data : dataList) {
//            if (data == null) continue;
//            double score = 0.0;
//            List<String> triggeredFactors = new ArrayList<>();
//            for (FactorMeta fm : factorMetas) {
//                //System.out.println("类型："+fm.type+" 值："+fm.triggerValues);
//                try {
//                    if (isTriggeredSafely(data, fm.type, fm.triggerValues)) {
//                        // 如果 factorValue 为 null，视为 0（保持原行为）
//                        Integer v = fm.config.getFactorValue();
//                        if (v != null) score += v;
//                        Double addSocre = (double) (v != null ? v : 0);
//                        triggeredFactors.add(fm.config.getFactorName() + "(+" + addSocre + ")");
//                    }
//                } catch (Exception ex) {
//                    // 单条因子判断异常不应中止整个批次，记录并继续
//                    System.err.println("判断因子触发时发生异常，因子ID=" + fm.config.getId() + "，记录ID=" + data.getId() + "，异常：" + ex.getMessage());
//                }
//            }
//
//
//            // 更新数据库风险分数（保留原名 updateLevel）
//            try {
//                vehicleMapper.updateLevel(data.getId(), score);
////                System.out.println("更新风险分数成功，ID=" + data.getId() + "，score=" + score);
//                // 输出触发详情（可选）
//                if (!triggeredFactors.isEmpty()) {
//                    System.out.println("ID=" + data.getId() + " 触发因子: " + String.join(", ", triggeredFactors) + "，总分=" + score);
//                } else {
//                    System.out.println("ID=" + data.getId() + " 未触发任何因子，总分=0");
//                }
//            } catch (Exception ex) {
//                // 更新失败记录并继续：避免更新失败导致整个批次停止
//                System.err.println("更新风险分数失败，ID=" + data.getId() + "，score=" + score + "，异常：" + ex.getMessage());
//            }
//
//            if (data.getId() > maxId) {
//                maxId = data.getId();
//            }
//        }
//
//        // 将最大已处理 id 写回文件（尽量保证原子性）
//        writeLastProcessedId(maxId);
//    }
//
//    /**
//     * 更健壮的触发判断器：统一捕获异常，将具体类型分派给对应逻辑
//     *
//     * @param data          数据实体
//     * @param factorType    枚举类型，来自 factorName
//     * @param triggerValues 触发值已被清洗（trim、去空）
//     * @return 是否触发
//     */
//    private boolean isTriggeredSafely(ToVehicleRealTimMonitoring data, FactorType factorType, List<String> triggerValues) {
//        if (triggerValues == null || triggerValues.isEmpty()) return false;
//        if (data == null) return false;
//
//        switch (factorType) {
//            case VEHICLE_TYPE:
//                return matchVehicleType(data, triggerValues);
//
//            case BRAND:
//                return matchBrand(data, triggerValues);
//
//            case Sub_BRAND:
//                return matchSubBrand(data, triggerValues);
//
//            case PLATE:
//                return matchPlate(data, triggerValues);
//
//            case FACE:
//                return matchFace(data, triggerValues);
//
//            case TIME:
//                return matchHourRange(data, triggerValues);
//
//            case MONTH:
//                return matchMonthRange(data, triggerValues);
//
//            case LOCATION:
//                return matchLocation(data, triggerValues);
//
//            case UNKNOWN:
//            default:
//                return false;
//        }
//    }
//
//    /* ------------------ 各因子具体匹配实现（保持与原逻辑一致/改进） ------------------ */
//
//    private boolean matchVehicleType(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        String vt = data.getVehicleType();
//        if (vt == null) return false;
//        // 精确匹配（与原代码一致）
//        return triggerValues.contains(vt);
//    }
//
//    private boolean matchBrand(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        String brandAll = data.getVehicleLogoAll();
//        if (brandAll == null) return false;
//        for (String val : triggerValues) {
//            if (val.isEmpty()) continue;
//            if (brandAll.contains(val)) return true;
//        }
//        return false;
//    }
//
//    private boolean matchSubBrand(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        String vehicleLogoAll = data.getVehicleLogoAll();
//        if (vehicleLogoAll == null || vehicleLogoAll.isEmpty()) return false;
//
//        // 提取 sub_brand（假设格式为 "品牌-子品牌"）
//        String[] parts = vehicleLogoAll.split("-");
//        if (parts.length < 2) return false; // 如果没有子品牌部分，直接返回 false
//        String subBrand = parts[1].trim();
//
//        // 检查提取的 sub_brand 是否在触发条件中
//        for (String triggerValue : triggerValues) {
//            if (subBrand.equalsIgnoreCase(triggerValue)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean matchPlate(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        String plate = data.getPlate();
//        if (plate == null) return false;
//        for (String val : triggerValues) {
//            if (val.isEmpty()) continue;
//            if (plate.startsWith(val)) return true;
//        }
//        return false;
//    }
//
//    private boolean matchFace(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        String pl = data.getPlateLicense();
//        if (pl == null) return false;
//        for (String val : triggerValues) {
//            if (val.isEmpty()) continue;
//            if (pl.contains(val)) return true;
//        }
//        return false;
//    }
//
//    /**
//     * 时间风险：触发值格式被假设为 "start-end"（小时，0-23）
//     * 原实现为 [start, end)（左闭右开）——这里我保留该语义（注释/文档中请说明）。
//     */
//    private boolean matchHourRange(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        if (data.getCaptureTime() == null) return false;
//        int hour = ZonedDateTime.ofInstant(data.getCaptureTime().toInstant(), DEFAULT_ZONE).getHour();
//        for (String val : triggerValues) {
//            if (val == null) continue;
//            String s = val.trim();
//            if (!s.contains("-")) continue;
//            int[] se = parseRangeSafe(s);
//            if (se == null) continue;
//            int start = se[0], end = se[1];
//            // 允许跨日区间，如 "22-2" 表示 22:00-次日02:00
//            if (start <= end) {
//                if (hour >= start && hour < end) return true;
//            } else {
//                // 跨日（例如 22-2）
//                if (hour >= start || hour < end) return true;
//            }
//        }
//        return false;
//    }
//
//    /**
//     * 月份风险：触发值格式 "start-end"（1-12）
//     * 原实现用了闭区间 [start, end]，保留该语义（注意与小时语义不同）。
//     */
//    private boolean matchMonthRange(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        if (data.getCaptureTime() == null) return false;
//        int month = ZonedDateTime.ofInstant(data.getCaptureTime().toInstant(), DEFAULT_ZONE).getMonthValue();
//        for (String val : triggerValues) {
//            if (val == null) continue;
//            String s = val.trim();
//            if (!s.contains("-")) continue;
//            int[] se = parseRangeSafe(s);
//            if (se == null) continue;
//            int start = se[0], end = se[1];
//            if (start <= end) {
//                if (month >= start && month <= end) return true;
//            } else {
//                // 跨年区间，例如 "11-2" 表示 11,12,1,2
//                if (month >= start || month <= end) return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean matchLocation(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
//        String dir = data.getDirection();
//        if (dir == null) return false;
//        for (String val : triggerValues) {
//            if (val.isEmpty()) continue;
//            if (dir.contains(val)) return true;
//        }
//        return false;
//    }
//
//    /**
//     * 解析 "start-end" 到 int[]{start,end}，若解析失败返回 null。
//     * 宽容处理：忽略空格、非数值等。
//     */
//    private int[] parseRangeSafe(String s) {
//        try {
//            String[] parts = s.split("-");
//            if (parts.length != 2) return null;
//            String a = parts[0].trim();
//            String b = parts[1].trim();
//            if (a.isEmpty() || b.isEmpty()) return null;
//            int start = Integer.parseInt(a);
//            int end = Integer.parseInt(b);
//            return new int[]{start, end};
//        } catch (Exception ex) {
//            // 记录解析错误但不抛出
//            System.err.println("解析范围失败: '" + s + "'，异常：" + ex.getMessage());
//            return null;
//        }
//    }
//
//    /* ------------------ 游标文件读写（使用 NIO，尽量原子写） ------------------ */
//
//    private long readLastProcessedId() {
//        Path p = Paths.get(POSITION_FILE);
//        if (!Files.exists(p)) return 0L;
//        try {
//            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
//            if (lines.isEmpty()) return 0L;
//            String first = lines.get(0).trim();
//            if (first.isEmpty()) return 0L;
//            return Long.parseLong(first);
//        } catch (Exception ex) {
//            // 出错时返回 0，避免抛出阻塞处理
//            System.err.println("读取游标文件失败，采用 0 作为起始 ID，异常：" + ex.getMessage());
//            return 0L;
//        }
//    }
//
//    private void writeLastProcessedId(long id) {
//        Path p = Paths.get(POSITION_FILE);
//        // 写入临时文件再原子替换，减少并发/中断时的损坏概率
//        Path tmp = p.resolveSibling(POSITION_FILE + ".tmp");
//        try {
//            Files.write(tmp, Collections.singleton(String.valueOf(id)), StandardCharsets.UTF_8,
//                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
//            // 原子替换（若文件系统支持）
//            Files.move(tmp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
//        } catch (AtomicMoveNotSupportedException amns) {
//            // fallback 到非原子替换
//            try {
//                Files.move(tmp, p, StandardCopyOption.REPLACE_EXISTING);
//            } catch (Exception ex) {
//                System.err.println("写游标文件失败（fallback），异常：" + ex.getMessage());
//            }
//        } catch (Exception ex) {
//            System.err.println("写游标文件失败，异常：" + ex.getMessage());
//        } finally {
//            // 清理遗留 tmp 文件
//            try {
//                if (Files.exists(tmp)) Files.deleteIfExists(tmp);
//            } catch (IOException ignored) {
//            }
//        }
//    }
//}

//这个版本加入案件车牌匹配
package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ToFactorConfig;
import com.ruoyi.system.domain.ToFactorTrigger;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.TobCaseHistory;
import com.ruoyi.system.mapper.ToFactorConfigMapper;
import com.ruoyi.system.mapper.ToFactorTriggerMapper;
import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;

import com.ruoyi.system.service.IToRiskScoreService;
import com.ruoyi.system.service.ITobCaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.*;

import java.util.stream.Collectors;

/**
 * 改进版风险分数计算服务（单文件实现）
 * - 更健壮的触发值解析与错误处理
 * - 使用枚举映射因子类型，减少对裸字符串的耦合
 * - 更安全的游标文件读写
 * - 支持车牌历史案件黑名单匹配
 */
@Service
public class ToRiskScoreServiceImpl implements IToRiskScoreService {

    private static final int BATCH_SIZE = 50;
    private static final String POSITION_FILE = "risk_position.txt";
    private static final ZoneId DEFAULT_ZONE = ZoneId.systemDefault();

    @Autowired
    private ToVehicleRealTimMonitoringMapper vehicleMapper;

    @Autowired
    private ToFactorConfigMapper factorConfigMapper;

    @Autowired
    private ToFactorTriggerMapper factorTriggerMapper;

    @Autowired
    private ITobCaseHistoryService caseHistoryService; // 新增：历史案件 Mapper

    // 预加载历史车牌集合（避免重复查询）
    private Set<String> historicalPlates = new HashSet<>();

    // 枚举定义（略，保持原样）
    private enum FactorType {
        VEHICLE_TYPE("车型风险"),
        BRAND("品牌风险"),
        PLATE("车牌风险"),
        FACE("人脸风险"),
        TIME("时间风险"),
        MONTH("月份风险"),
        LOCATION("地点风险"),
        Sub_BRAND("子品牌风险"),
        UNKNOWN("UNKNOWN");

        private final String displayName;

        FactorType(String displayName) {
            this.displayName = displayName;
        }

        public static FactorType fromName(String name) {
            if (name == null) return UNKNOWN;
            String trimmed = name.trim();
            for (FactorType ft : values()) {
                if (ft.displayName.equalsIgnoreCase(trimmed)) {
                    return ft;
                }
            }
            if (trimmed.contains("car_type_factor")) return VEHICLE_TYPE;
            if (trimmed.contains("brand_factor")) return BRAND;
            if (trimmed.contains("sub_brand_factor")) return Sub_BRAND;
            if (trimmed.contains("LPN_factor")) return PLATE;
            if (trimmed.contains("face_factor")) return FACE;
            if (trimmed.contains("time_factor") && !trimmed.contains("月")) return TIME;
            if (trimmed.contains("month_factor")) return MONTH;
            if (trimmed.contains("location_factor") || trimmed.contains("方向")) return LOCATION;
            return UNKNOWN;
        }
    }

    @Override
    public void processNextBatch() {
        long lastId = readLastProcessedId();
        List<ToVehicleRealTimMonitoring> dataList = vehicleMapper.selectAfterId(lastId, BATCH_SIZE);
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        // 预加载历史车牌（只在首次或需要时刷新）
        loadHistoricalPlates();

        List<ToFactorConfig> factors = factorConfigMapper.selectFactorConfigList(null);
        List<ToFactorTrigger> triggers = factorTriggerMapper.selectFactorTriggerList(null);

        Map<Long, List<String>> factorTriggersMap = triggers.stream()
                .filter(Objects::nonNull)
                .collect(Collectors.groupingBy(ToFactorTrigger::getFactorId,
                        Collectors.mapping(ToFactorTrigger::getTriggerValue, Collectors.toList())));

        long maxId = lastId;

        // 预解析因子元数据
        class FactorMeta {
            final ToFactorConfig config;
            final FactorType type;
            final List<String> triggerValues;

            FactorMeta(ToFactorConfig config, FactorType type, List<String> triggerValues) {
                this.config = config;
                this.type = type;
                this.triggerValues = triggerValues;
            }
        }

        List<FactorMeta> factorMetas = new ArrayList<>();
        for (ToFactorConfig factor : factors) {
            if (factor == null) continue;
            FactorType type = FactorType.fromName(factor.getFactorName());
            List<String> tv = factorTriggersMap.getOrDefault(factor.getId(), Collections.emptyList());
            List<String> cleaned = tv.stream()
                    .filter(Objects::nonNull)
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .collect(Collectors.toList());
            factorMetas.add(new FactorMeta(factor, type, cleaned));
        }

        for (ToVehicleRealTimMonitoring data : dataList) {
            if (data == null) continue;
            double score = 0.0;
            List<String> triggeredFactors = new ArrayList<>();

            for (FactorMeta fm : factorMetas) {
                try {
                    if (isTriggeredSafely(data, fm.type, fm.triggerValues)) {
                        Integer v = fm.config.getFactorValue();
                        if (v != null) score += v;
                        Double addSocre = (double) (v != null ? v : 0);
                        triggeredFactors.add(fm.config.getFactorName() + "(+" + addSocre + ")");
                    }
                } catch (Exception ex) {
                    System.err.println("判断因子触发时发生异常，因子ID=" + fm.config.getId() + "，记录ID=" + data.getId() + "，异常：" + ex.getMessage());
                }
            }

            try {
                vehicleMapper.updateLevel(data.getId(), score);
                if (!triggeredFactors.isEmpty()) {
                    System.out.println("ID=" + data.getId() + " 触发因子: " + String.join(", ", triggeredFactors) + "，总分=" + score);
                } else {
                    System.out.println("ID=" + data.getId() + " 未触发任何因子，总分=0");
                }
            } catch (Exception ex) {
                System.err.println("更新风险分数失败，ID=" + data.getId() + "，score=" + score + "，异常：" + ex.getMessage());
            }

            if (data.getId() > maxId) {
                maxId = data.getId();
            }
        }

        writeLastProcessedId(maxId);
    }

    private void loadHistoricalPlates() {
        if (!historicalPlates.isEmpty()) return;

        try {
            List<TobCaseHistory> histories = caseHistoryService.listAll();
            if (histories != null) {
                historicalPlates.addAll(histories.stream()
                        .map(TobCaseHistory::getHisCarPlate)
                        .filter(Objects::nonNull)
                        .map(String::trim)
                        .collect(Collectors.toSet()));
            }
            System.out.println("✅ 已加载历史案件车牌数: " + historicalPlates.size());
        } catch (Exception e) {
            System.err.println("加载历史车牌失败：" + e.getMessage());
        }
    }

    private boolean isTriggeredSafely(ToVehicleRealTimMonitoring data, FactorType factorType, List<String> triggerValues) {
        //if (triggerValues == null || triggerValues.isEmpty()) return false;
        if (data == null) return false;
        switch (factorType) {
            case VEHICLE_TYPE:
            case BRAND:
            case Sub_BRAND:
            case FACE:
            case TIME:
            case MONTH:
            case LOCATION:
                // 这些因子依赖 triggerValues
                if (triggerValues == null || triggerValues.isEmpty()) return false;
                break;

            case PLATE:
                // 车牌风险不依赖 triggerValues，只看历史案件
                // 所以不检查 triggerValues 是否为空
                break;

            default:
                return false;
        }
        switch (factorType) {
            case VEHICLE_TYPE:
                return matchVehicleType(data, triggerValues);

            case BRAND:
                return matchBrand(data, triggerValues);

            case Sub_BRAND:
                return matchSubBrand(data, triggerValues);

            case PLATE:
                return matchPlate(data, triggerValues); // 新增：支持黑名单匹配

            case FACE:
                return matchFace(data, triggerValues);

            case TIME:
                return matchHourRange(data, triggerValues);

            case MONTH:
                return matchMonthRange(data, triggerValues);

            case LOCATION:
                return matchLocation(data, triggerValues);

            default:
                return false;
        }
    }

    /* ------------------ 各因子具体匹配实现 ------------------ */

    private boolean matchVehicleType(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        String vt = data.getVehicleType();
        return vt != null && triggerValues.contains(vt);
    }

    private boolean matchBrand(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        String brandAll = data.getVehicleLogoAll();
        return brandAll != null && triggerValues.stream().anyMatch(brandAll::contains);
    }

    private boolean matchSubBrand(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        String vehicleLogoAll = data.getVehicleLogoAll();
        if (vehicleLogoAll == null || vehicleLogoAll.isEmpty()) return false;

        String[] parts = vehicleLogoAll.split("-");
        if (parts.length < 2) return false;
        String subBrand = parts[1].trim();

        return triggerValues.stream().anyMatch(subBrand::equalsIgnoreCase);
    }

    /**
     * 车牌风险匹配：支持两种方式
     * 1. 配置触发值（前缀匹配，如 "京A"）
     * 2. 是否在历史案件黑名单中（精确匹配）
     */
    private boolean matchPlate(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        String plate = data.getPlate();
        if (plate == null) return false;

        // 方式一：配置触发值（前缀匹配）
        for (String val : triggerValues) {
            if (val.isEmpty()) continue;
            if (plate.startsWith(val)) return true;
        }

        // 方式二：是否在历史案件中（精确匹配）

//        if (historicalPlates.contains(plate)) {
//            return true;
//        }
//
//        return false;
        plate = plate.trim();

        return historicalPlates.contains(plate);
    }

    private boolean matchFace(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        String pl = data.getPlateLicense();
        return pl != null && triggerValues.stream().anyMatch(pl::contains);
    }

    private boolean matchHourRange(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        if (data.getCaptureTime() == null) return false;
        int hour = ZonedDateTime.ofInstant(data.getCaptureTime().toInstant(), DEFAULT_ZONE).getHour();
        for (String val : triggerValues) {
            if (val == null) continue;
            String s = val.trim();
            if (!s.contains("-")) continue;
            int[] se = parseRangeSafe(s);
            if (se == null) continue;
            int start = se[0], end = se[1];
            if (start <= end) {
                if (hour >= start && hour < end) return true;
            } else {
                if (hour >= start || hour < end) return true;
            }
        }
        return false;
    }

    private boolean matchMonthRange(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        if (data.getCaptureTime() == null) return false;
        int month = ZonedDateTime.ofInstant(data.getCaptureTime().toInstant(), DEFAULT_ZONE).getMonthValue();
        for (String val : triggerValues) {
            if (val == null) continue;
            String s = val.trim();
            if (!s.contains("-")) continue;
            int[] se = parseRangeSafe(s);
            if (se == null) continue;
            int start = se[0], end = se[1];
            if (start <= end) {
                if (month >= start && month <= end) return true;
            } else {
                if (month >= start || month <= end) return true;
            }
        }
        return false;
    }

    private boolean matchLocation(ToVehicleRealTimMonitoring data, List<String> triggerValues) {
        String dir = data.getDirection();
        return dir != null && triggerValues.stream().anyMatch(dir::contains);
    }

    private int[] parseRangeSafe(String s) {
        try {
            String[] parts = s.split("-");
            if (parts.length != 2) return null;
            String a = parts[0].trim();
            String b = parts[1].trim();
            if (a.isEmpty() || b.isEmpty()) return null;
            int start = Integer.parseInt(a);
            int end = Integer.parseInt(b);
            return new int[]{start, end};
        } catch (Exception ex) {
            System.err.println("解析范围失败: '" + s + "'，异常：" + ex.getMessage());
            return null;
        }
    }

    /* ------------------ 游标文件读写 ------------------ */

    private long readLastProcessedId() {
        Path p = Paths.get(POSITION_FILE);
        if (!Files.exists(p)) return 0L;
        try {
            List<String> lines = Files.readAllLines(p, StandardCharsets.UTF_8);
            if (lines.isEmpty()) return 0L;
            String first = lines.get(0).trim();
            if (first.isEmpty()) return 0L;
            return Long.parseLong(first);
        } catch (Exception ex) {
            System.err.println("读取游标文件失败，采用 0 作为起始 ID，异常：" + ex.getMessage());
            return 0L;
        }
    }

    private void writeLastProcessedId(long id) {
        Path p = Paths.get(POSITION_FILE);
        Path tmp = p.resolveSibling(POSITION_FILE + ".tmp");
        try {
            Files.write(tmp, Collections.singleton(String.valueOf(id)), StandardCharsets.UTF_8,
                    StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            Files.move(tmp, p, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
        } catch (AtomicMoveNotSupportedException amns) {
            try {
                Files.move(tmp, p, StandardCopyOption.REPLACE_EXISTING);
            } catch (Exception ex) {
                System.err.println("写游标文件失败（fallback），异常：" + ex.getMessage());
            }
        } catch (Exception ex) {
            System.err.println("写游标文件失败，异常：" + ex.getMessage());
        } finally {
            try {
                if (Files.exists(tmp)) Files.deleteIfExists(tmp);
            } catch (IOException ignored) {
            }
        }
    }
}