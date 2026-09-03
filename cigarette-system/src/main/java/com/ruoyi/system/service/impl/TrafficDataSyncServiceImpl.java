package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.*;
import com.ruoyi.system.domain.cache.*;
import com.ruoyi.system.domain.vo.TobStaffVo;
import com.ruoyi.system.mapper.*;
import com.ruoyi.system.service.TrafficDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Service
public class TrafficDataSyncServiceImpl implements TrafficDataSyncService {

    private static final int BATCH = 1000;
    private long waterMark = 0L;

    @Autowired
    private TrafficDataMapper trafficDataMapper;
    @Autowired
    private TobCacheMapper cacheMapper;
    @Autowired
    private TobSuspicionLevelMapper tobSuspicionLevelMapper;

    // 新增：短信需要的Mapper
    @Autowired
    private TobCameraMapper tobCameraMapper;
    @Autowired
    private TobClockLogMapper tobClockLogMapper;
    @Autowired
    private TobStaffMapper tobStaffMapper;
    @Autowired
    private SmsService smsService;
    @Autowired
    private TobDetectionMapper tobDetectionMapper;

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        TobCacheDaySlice slice = cacheMapper.selectDaySliceOne();
        waterMark = slice == null || slice.getMaxTrafficId() == null
                ? 0L : slice.getMaxTrafficId();
        if (tobSuspicionLevelMapper.selectSuspicionLevel() == null) {
            tobSuspicionLevelMapper.insertSuspicionLevel(0.0);
        }
    }

    @Override
    @Transactional
    public void sync() {
        TrafficData param = new TrafficData();
        param.setId(waterMark);
        param.setLevel(0.0);
        List<TrafficData> news = trafficDataMapper.nextTenList(param);
        if (news == null || news.isEmpty()) return;

        System.out.println("[SYNC] 发现 " + news.size() + " 条新数据, waterMark=" + waterMark + ", 第一条Id=" + news.get(0).getId());

        // 以下是原有的缓存统计代码，一行不改
        long batchMaxId = waterMark;
        String nowYM = sdf.format(new Date()).substring(0, 7);
        double curLevel = 0.0;
        TobSuspicionLevel po = tobSuspicionLevelMapper.selectSuspicionLevel();
        if (po != null && po.getLevel() != null) {
            curLevel = po.getLevel();
        }
//        System.out.println("【当前嫌疑阈值】" + curLevel);
        for (TrafficData t : news) {
            batchMaxId = Math.max(batchMaxId, t.getId());

            TobCacheCamera camera = buildCamera(t, curLevel);
            TobCacheDaynight dayN = buildDaynight(t, curLevel);
            TobCacheDaySlice slice = buildDaySlice(t, batchMaxId, nowYM, curLevel);
            TobCacheHour hour = buildHour(t, curLevel);
            TobCacheSeason season = buildSeason(t, curLevel);
            TobCacheVehicleColor color = buildColor(t, curLevel);
            TobCacheVehicleType type = buildType(t, curLevel);

            cacheMapper.incCamera(camera);
            cacheMapper.incDaynight(dayN);
            cacheMapper.incDaySlice(slice);
            cacheMapper.incHour(hour);
            cacheMapper.incSeason(season);
            cacheMapper.incVehicleColor(color);
            cacheMapper.incVehicleType(type);
        }
        waterMark = batchMaxId;

        //新增：短信告警逻辑
        for (TrafficData item : news) {
            try {
                sendAlert(item);
            } catch (Exception e) {
//                System.err.println("短信发送失败，车辆ID: " + item.getId() + ", 错误: " + e.getMessage());
            }
        }
    }

    /**
     * 短信发送逻辑
     */
    private void sendAlert(TrafficData item) {
        try {
//            System.out.println("========== [SMS] 开始处理 车辆ID=" + item.getId() + " CameraId=" + item.getCameraId() + " Level=" + item.getLevel() + " Time=" + item.getCaptureTime() + " ==========");

            // 当没有这个监测点时返回
            if (item.getCameraId() == null) return;
            TobDetection detectionId = tobDetectionMapper.selectTobDetectionByDetectionId((long) item.getCameraId());

            // 当该监测点没有人员或设备时返回
            List<String> list = tobClockLogMapper.selectPhoneNumbersByDetectionId(detectionId.getDetectionId());
            if (list == null && list.isEmpty()) return;
            // 查询当前车辆是否存在预警处理
            if (item.getLevel() <= 40) return;
            if (item.getCaptureTime() == null || item.getCaptureTime().isEmpty()) return;

            // 兼容datetime(6)：JDBC可能返回6位小数、3位小数、或无小数
            String captureTimeStr = item.getCaptureTime();
//            System.out.println("[SMS] 原始CaptureTime=[" + captureTimeStr + "] 长度=" + (captureTimeStr != null ? captureTimeStr.length() : 0));
            if (captureTimeStr != null) {
                if (!captureTimeStr.contains(".")) {
                    captureTimeStr = captureTimeStr + ".000";
                } else if (captureTimeStr.length() > 23) {
                    captureTimeStr = captureTimeStr.substring(0, 23);
                }
            }
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
            Date captureDate = sdf.parse(captureTimeStr);

            String message = buildAlertMessage(item, detectionId.getDetectionName());
            System.out.println("[SMS] 短信内容: " + message);

            for (String phone : list) {
                boolean success = smsService.sendSms(phone, message);
                if (success) {
                    System.out.println("[SMS] ✅✅✅ 短信发送成功! 车辆ID=" + item.getId() + " 手机号=" + phone);
                } else {
                    System.out.println("[SMS] ❌ 短信发送失败 车辆ID=" + item.getId());
                }
            }

        } catch (Exception e) {
            System.err.println("[SMS] ❌❌❌ 异常 车辆ID=" + item.getId() + " 错误: " + e.getMessage());
            e.printStackTrace();
        }
    }
    //
//    这是原来的短信模块备份
//    private void sendAlert(TrafficData item) {
//        try {
//            System.out.println("========== [SMS] 开始处理 车辆ID=" + item.getId() + " CameraId=" + item.getCameraId() + " Level=" + item.getLevel() + " Time=" + item.getCaptureTime() + " ==========");
//
//            if (item.getCameraId() == null) {
//                System.out.println("[SMS] ❌ 摄像头ID为空，跳过");
//                return;
//            }
//
//            TobCamera camera = tobCameraMapper.selectByTrafficCameraId(item.getCameraId().longValue());
//            if (camera == null) {
//                System.out.println("[SMS] ❌ 未找到摄像头 traffic_camera_id=" + item.getCameraId());
//                return;
//            }
//            System.out.println("[SMS] ✅ 找到摄像头 detection_id=" + camera.getDetectionId());
//
//            if (item.getCaptureTime() == null || item.getCaptureTime().isEmpty()) {
//                System.out.println("[SMS] ❌ 捕获时间为空，跳过");
//                return;
//            }
//            // 兼容datetime(6)：JDBC可能返回6位小数、3位小数、或无小数
//            String captureTimeStr = item.getCaptureTime();
//            System.out.println("[SMS] 原始CaptureTime=[" + captureTimeStr + "] 长度=" + (captureTimeStr != null ? captureTimeStr.length() : 0));
//            if (captureTimeStr != null) {
//                if (!captureTimeStr.contains(".")) {
//                    captureTimeStr = captureTimeStr + ".000";
//                } else if (captureTimeStr.length() > 23) {
//                    captureTimeStr = captureTimeStr.substring(0, 23);
//                }
//                System.out.println("[SMS] 规范化后=[" + captureTimeStr + "]");
//            }
//            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
//            Date captureDate = sdf.parse(captureTimeStr);
//            System.out.println("[SMS] ✅ 时间解析成功: " + captureDate);
//
//            TobClockLog duty = tobClockLogMapper.selectCurrentDuty(
//                    camera.getDetectionId(), captureDate
//            );
//
//            if (duty == null) {
//                System.out.println("[SMS] ❌ 未找到值班记录 detection_id=" + camera.getDetectionId() + " time=" + captureTimeStr);
//                return;
//            }
//            System.out.println("[SMS] ✅ 找到值班记录 staff_id=" + duty.getStaffId() + " start=" + duty.getStartTime() + " end=" + duty.getEndTime());
//
//            TobStaffVo staff = tobStaffMapper.selectByIdWithPhone(duty.getStaffId());
//            if (staff == null) {
//                System.out.println("[SMS] ❌ 未找到工作人员 staff_id=" + duty.getStaffId());
//                return;
//            }
//            System.out.println("[SMS] ✅ 找到工作人员 user_id=" + staff.getUserId() + " phone=" + staff.getPhonenumber());
//
//            String phone = staff.getPhonenumber();
////            String phone = "13150566150";
//            if (phone == null || phone.isEmpty()) {
//                System.out.println("[SMS] ❌ 手机号为空 staff_id=" + duty.getStaffId());
//                return;
//            }
//
//            String message = buildAlertMessage(item);
//            System.out.println("[SMS] 短信内容: " + message);
//
//            boolean success = smsService.sendSms(phone, message);
//            if (success) {
//                System.out.println("[SMS] ✅✅✅ 短信发送成功! 车辆ID=" + item.getId() + " 手机号=" + phone);
//            } else {
//                System.out.println("[SMS] ❌ 短信接口返回失败 车辆ID=" + item.getId());
//            }
//
//        } catch (Exception e) {
//            System.err.println("[SMS] ❌❌❌ 异常 车辆ID=" + item.getId() + " 错误: " + e.getMessage());
//            e.printStackTrace();
//        }
//    }

    /**
     * 新增：构建短信内容
     */
    private String buildAlertMessage(TrafficData item, String detectionName) {
        StringBuilder sb = new StringBuilder();
        // 改成平台允许的签名之一
        sb.append("【云南省烟草公司曲靖市公司】");
        sb.append("车牌：").append(item.getPlate());
        sb.append("（").append(item.getVehicleType()).append("-").append(item.getVehicleLogo()).append("）");
        String rawTime = item.getCaptureTime();
        String timeOnly = "";
        if (rawTime != null) {
            // 按空格分割，取第二部分（时间部分）
            String[] spaceParts = rawTime.split(" ");
            if (spaceParts.length >= 2) {
                String timeWithMillis = spaceParts[1];
                // 按小数点分割，取第一部分
                String[] dotParts = timeWithMillis.split("\\.");
                timeOnly = dotParts[0]; // "14:58:07"
            }
        }
        LocalTime originalTime = LocalTime.parse(timeOnly, DateTimeFormatter.ofPattern("HH:mm:ss"));
        // 将时间减少4.35分钟用于采集平衡系统与真是数据的差值（已修复）
//        LocalTime newTime = originalTime.minusSeconds(4 * 60 + 35);
        String finalTime = originalTime.format(DateTimeFormatter.ofPattern("HH:mm:ss"));

        sb.append("于").append(finalTime);
        sb.append("经过").append(detectionName.trim()).append("，");

        String level;
        if (item.getLevel() >= 60) {
            level = "高";
        } else if (item.getLevel() >= 40 && item.getLevel() < 60) {
            level = "中";
        } else {
            level = "低";
        }

        sb.append("预警等级：").append(level).append("。");
//        sb.append("风险因子：夜间行驶、历史涉烟").append("。");
        sb.append("请拦截！");
        return sb.toString();
    }

    /* ------------------- 以下都是你原有的缓存统计方法，一字未改 ------------------- */

    private TobCacheCamera buildCamera(TrafficData t, double level) {
        TobCacheCamera c = new TobCacheCamera();
        if (t.getLevel() == null || t.getLevel() < level) {
            return c;
        }

        String name = t.getCameraName();
        if (name == null) {
            c.setOther(1L);
            return c;
        }
        name = name.trim();
        switch (name) {
            case "板桥镇326国道中国石化加油站旁卡口":
                c.setBanqiao326Gas(1L);
                break;
            case "老地方饭店附近卡口":
                c.setLaoDifang(1L);
                break;
            case "G248国道与西响线交叉口南向卡口":
                c.setG248Xixiang(1L);
                break;
            case "板桥收费站出口一体机":
                c.setBanqiaoExit(1L);
                break;
            case "陆良县326国道经官中学附近卡口":
                c.setLuliangKaikou(1L);
                break;
            case "罗平县阿岗收费站出口卡口":
                c.setLuopingAgang(1L);
                break;
            default:
                c.setOther(1L);
                break;
        }
        return c;
    }

    private TobCacheDaynight buildDaynight(TrafficData t, double level) {
        TobCacheDaynight n = new TobCacheDaynight();
        if (t.getLevel() == null || t.getLevel() < level) {
            return n;
        }
        int h = hourOf(t.getCaptureTime());
        if (h >= 6 && h < 18) n.setDayNum(1L);
        else n.setNightNum(1L);
        return n;
    }

    private TobCacheDaySlice buildDaySlice(TrafficData t, long currId, String nowYM, double level) {
        TobCacheDaySlice s = new TobCacheDaySlice();
        int days = daysBeforeToday(t.getCaptureTime());

        if (days >= 0 && days <= 7) {
            switch (days) {
                case 0:
                    s.setD0(1L);
                    break;
                case 1:
                    s.setD1(1L);
                    break;
                case 2:
                    s.setD2(1L);
                    break;
                case 3:
                    s.setD3(1L);
                    break;
                case 4:
                    s.setD4(1L);
                    break;
                case 5:
                    s.setD5(1L);
                    break;
                case 6:
                    s.setD6(1L);
                    break;
                case 7:
                    s.setD7(1L);
                    break;
            }
        }
        if (t.getCaptureTime().substring(0, 7).equals(nowYM)) {
            s.setMonthCurr(1L);
        }

        boolean sus = t.getLevel() != null && t.getLevel() >= level;
        if (sus) {
            if (days >= 0 && days <= 7) {
                switch (days) {
                    case 0:
                        s.setSuspectD0(1L);
                        break;
                    case 1:
                        s.setSuspectD1(1L);
                        break;
                    case 2:
                        s.setSuspectD2(1L);
                        break;
                    case 3:
                        s.setSuspectD3(1L);
                        break;
                    case 4:
                        s.setSuspectD4(1L);
                        break;
                    case 5:
                        s.setSuspectD5(1L);
                        break;
                    case 6:
                        s.setSuspectD6(1L);
                        break;
                    case 7:
                        s.setSuspectD7(1L);
                        break;
                }
            }
            if (t.getCaptureTime().substring(0, 7).equals(nowYM)) {
                s.setSuspectMonthCurr(1L);
            }
        }

        s.setMaxTrafficId(currId);
        return s;
    }

    private TobCacheHour buildHour(TrafficData t, double level) {
        TobCacheHour h = new TobCacheHour();
        if (t.getLevel() == null || t.getLevel() < level) {
            return h;
        }
        int hour = hourOf(t.getCaptureTime());
        switch (hour) {
            case 0:
                h.setH00(1L);
                break;
            case 1:
                h.setH01(1L);
                break;
            case 2:
                h.setH02(1L);
                break;
            case 3:
                h.setH03(1L);
                break;
            case 4:
                h.setH04(1L);
                break;
            case 5:
                h.setH05(1L);
                break;
            case 6:
                h.setH06(1L);
                break;
            case 7:
                h.setH07(1L);
                break;
            case 8:
                h.setH08(1L);
                break;
            case 9:
                h.setH09(1L);
                break;
            case 10:
                h.setH10(1L);
                break;
            case 11:
                h.setH11(1L);
                break;
            case 12:
                h.setH12(1L);
                break;
            case 13:
                h.setH13(1L);
                break;
            case 14:
                h.setH14(1L);
                break;
            case 15:
                h.setH15(1L);
                break;
            case 16:
                h.setH16(1L);
                break;
            case 17:
                h.setH17(1L);
                break;
            case 18:
                h.setH18(1L);
                break;
            case 19:
                h.setH19(1L);
                break;
            case 20:
                h.setH20(1L);
                break;
            case 21:
                h.setH21(1L);
                break;
            case 22:
                h.setH22(1L);
                break;
            case 23:
                h.setH23(1L);
                break;
            default:
                break;
        }
        return h;
    }

    private TobCacheSeason buildSeason(TrafficData t, double level) {
        TobCacheSeason s = new TobCacheSeason();
        if (t.getLevel() == null || t.getLevel() < level) {
            return s;
        }
        int m = monthOf(t.getCaptureTime());
        if (m >= 3 && m <= 5) {
            s.setSpring(1L);
        } else if (m >= 6 && m <= 8) {
            s.setSummer(1L);
        } else if (m >= 9 && m <= 11) {
            s.setAutumn(1L);
        } else {
            s.setWinter(1L);
        }
        return s;
    }

    private TobCacheVehicleColor buildColor(TrafficData t, double level) {
        TobCacheVehicleColor c = new TobCacheVehicleColor();
        if (t.getLevel() == null || t.getLevel() < level) {
            return c;
        }
        String v = t.getVehicleColor();
        if (v == null) {
            c.setOtherC(1L);
            return c;
        }
        if (v.contains("白色")) {
            c.setWhite(1L);
        } else if (v.contains("黑色")) {
            c.setBlack(1L);
        } else if (v.contains("灰色")) {
            c.setGray(1L);
        } else if (v.contains("红色")) {
            c.setRed(1L);
        } else if (v.contains("蓝色") && !v.contains("深蓝色")) {
            c.setBlue(1L);
        } else if (v.contains("棕色") || v.contains("褐色")) {
            c.setBrown(1L);
        } else if (v.contains("银色")) {
            c.setSilver(1L);
        } else if (v.contains("黄色")) {
            c.setYellow(1L);
        } else if (v.contains("绿色")) {
            c.setGreen(1L);
        } else if (v.contains("深蓝色")) {
            c.setDeepBlue(1L);
        } else {
            c.setOtherC(1L);
        }
        return c;
    }

    private TobCacheVehicleType buildType(TrafficData t, double level) {
        TobCacheVehicleType v = new TobCacheVehicleType();
        if (t.getLevel() == null || t.getLevel() < level) {
            return v;
        }
        String type = t.getVehicleType();
        if (type == null) {
            v.setOther(1L);
            return v;
        }
        type = type.trim();
        switch (type) {
            case "轿车":
                v.setSedan(1L);
                break;
            case "SUV/MPV":
                v.setSuvMpv(1L);
                break;
            case "货车":
                v.setTruck(1L);
                break;
            case "二轮车":
                v.setTwoWheeler(1L);
                break;
            case "面包车":
                v.setVan(1L);
                break;
            case "小货车":
                v.setLightTruck(1L);
                break;
            case "三轮车":
                v.setThreeWheeler(1L);
                break;
            case "小型车":
                v.setSmallCar(1L);
                break;
            case "行人":
                v.setPedestrian(1L);
                break;
            case "大型车":
                v.setLargeVehicle(1L);
                break;
            case "皮卡车":
                v.setPickup(1L);
                break;
            case "大型客车":
                v.setLargeBus(1L);
                break;
            case "未知":
                v.setUnknown(1L);
                break;
            case "非机动车":
                v.setNonMotor(1L);
                break;
            case "中型客车":
                v.setMediumBus(1L);
                break;
            case "suv":
                v.setSuvOnly(1L);
                break;
            default:
                v.setOther(1L);
                break;
        }
        return v;
    }

    private int hourOf(String dateStr) {
        try {
            Date d = sdf.parse(dateStr);
            return d.getHours();
        } catch (ParseException e) {
            return 0;
        }
    }

    private int monthOf(String dateStr) {
        try {
            Date d = sdf.parse(dateStr);
            return d.getMonth() + 1;
        } catch (ParseException e) {
            return 1;
        }
    }

    private int daysBeforeToday(String captureTime) {
        try {
            Date cap = sdf.parse(captureTime);
            Date today = new Date();
            today = sdf.parse(sdf.format(today));
            long diff = today.getTime() - cap.getTime();
            int days = (int) (diff / (24 * 60 * 60 * 1000));
            return days >= 0 && days <= 7 ? days : -1;
        } catch (ParseException e) {
            return -1;
        }
    }

    @Scheduled(cron = "0 0 0 * * ?")
    public void dayShiftJob() {
        cacheMapper.dayShift();
    }

    @Scheduled(cron = "0 0 0 1 * ?")
    public void monthResetJob() {
        cacheMapper.monthReset();
    }
}