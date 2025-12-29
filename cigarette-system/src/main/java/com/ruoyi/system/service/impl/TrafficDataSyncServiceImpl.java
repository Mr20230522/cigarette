package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobSuspicionLevel;
import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.cache.*;
import com.ruoyi.system.mapper.TobCacheMapper;
import com.ruoyi.system.mapper.TobSuspicionLevelMapper;
import com.ruoyi.system.mapper.TrafficDataMapper;
import com.ruoyi.system.service.TrafficDataSyncService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.text.ParseException;
import java.text.SimpleDateFormat;
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


    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        TobCacheDaySlice slice = cacheMapper.selectDaySliceOne();
        System.out.println("【init】slice = " + slice);
        System.out.println("【init】maxTrafficId = " +
                (slice == null ? "null" : slice.getMaxTrafficId()));
        waterMark = slice == null || slice.getMaxTrafficId() == null
                ? 0L : slice.getMaxTrafficId();
        if (tobSuspicionLevelMapper.selectSuspicionLevel() == null) {
            tobSuspicionLevelMapper.insertSuspicionLevel(0.0); // 默认 0 全放行，也可写 0.7
        }
    }

    @Override
    @Transactional
    public void sync() {
        TrafficData param = new TrafficData();
        param.setId(waterMark);
        param.setLevel(0.0);          // 触发 SQL 里 level>=0
        List<TrafficData> news = trafficDataMapper.nextTenList(param);
        if (news == null || news.isEmpty()) return;

        long batchMaxId = waterMark;
        String nowYM = sdf.format(new Date()).substring(0, 7); // 循环外只算一次
//        Double curLevel = tobSuspicionLevelMapper.selectSuspicionLevel().getLevel();
        // 原来直接 .getLevel()
        double curLevel = 0.0;
        TobSuspicionLevel po = tobSuspicionLevelMapper.selectSuspicionLevel();
        if (po != null && po.getLevel() != null) {
            curLevel = po.getLevel();
        }
        System.out.println("【当前嫌疑阈值】" + curLevel);
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
    }

    /* ------------------- 构建：每条数据 new 一个新对象 ------------------- */

    private TobCacheCamera buildCamera(TrafficData t, double level) {
        TobCacheCamera c = new TobCacheCamera();
        /* 不达标直接返回 */
        if (t.getLevel() == null || t.getLevel() < level) {
            return c;          // 全是 null，不加任何计数
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
            return n;          // 全是 null，不加任何计数
        }
        int h = hourOf(t.getCaptureTime());
        if (h >= 6 && h < 18) n.setDayNum(1L);
        else n.setNightNum(1L);
        return n;
    }

    //
//        private TobCacheDaySlice buildDaySlice(TrafficData t, long currId) {
//        TobCacheDaySlice s = new TobCacheDaySlice();
//        // 无论数据是哪天的，业务只负责「今天」这一笔
//        s.setD0(1L);               // 永远写今天
//        s.setMonthCurr(1L);        // 每条+1，月累加自动涨
//        s.setMaxTrafficId(currId);
//        return s;
//    }
    private TobCacheDaySlice buildDaySlice(TrafficData t, long currId, String nowYM, double level) {
        TobCacheDaySlice s = new TobCacheDaySlice();
        int days = daysBeforeToday(t.getCaptureTime());

        /* ① 先写普通计数（不论嫌疑） */
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

        /* ② 再补嫌疑计数（仅当达标） */
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
            return h;          // 全是 null，不加任何计数
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
            return s;          // 全是 null，不加任何计数
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
            return c;          // 全是 null，不加任何计数
        }
        String v = t.getVehicleColor();
        if (v == null) {
            c.setOtherC(1L);
            return c;
        }
        // 只认“白色”“黑色”“灰色”……这种完整词
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
        } else if (v.contains("深蓝色")) {   // 复合色放最后
            c.setDeepBlue(1L);
        } else {
            c.setOtherC(1L);
        }
        return c;
    }

    private TobCacheVehicleType buildType(TrafficData t, double level) {
        TobCacheVehicleType v = new TobCacheVehicleType();
        if (t.getLevel() == null || t.getLevel() < level) {
            return v;          // 全是 null，不加任何计数
        }
        String type = t.getVehicleType();
        if (type == null) {
            v.setOther(1L);
            return v;
        }
        type = type.trim();          // 去掉前后空格

        // 必须完全相等，大小写随意（都已 trim）
        switch (type) {
            case "轿车":
                v.setSedan(1L);
                break;
            case "SUV/MPV":                      // 组合值单独一列
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
            case "suv":          // 纯 suv
                v.setSuvOnly(1L);
                break;
            default:
                v.setOther(1L);   // 任何未定义车型
                break;
        }
        return v;
    }

    /* ------------------- 时间工具（JDK8 兼容） ------------------- */

    private int hourOf(String dateStr) {
        try {
            Date d = sdf.parse(dateStr);
            return d.getHours();
        } catch (ParseException e) {
            return 0;
        }
    }

    private int dayOfMonth(String dateStr) {
        try {
            Date d = sdf.parse(dateStr);
            return d.getDate();
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

    /**
     * 计算 captureTime 距「今天」的天数差
     * 0 = 今天，1 = 昨天 … 7 = 7 天前，负数或 >7 返回 -1（丢弃）
     */
    private int daysBeforeToday(String captureTime) {
        try {
            Date cap = sdf.parse(captureTime);
            Date today = new Date();          // 系统当天 00:00:00
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
//package com.ruoyi.system.service.impl;
//
//import com.ruoyi.system.domain.TobSuspicionLevel;
//import com.ruoyi.system.domain.TrafficData;
//import com.ruoyi.system.domain.cache.*;
//import com.ruoyi.system.mapper.TobCacheMapper;
//import com.ruoyi.system.mapper.TobSuspicionLevelMapper;
//import com.ruoyi.system.mapper.TrafficDataMapper;
//import com.ruoyi.system.service.TrafficDataSyncService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.scheduling.annotation.Scheduled;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//
//import javax.annotation.PostConstruct;
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.List;
//
//@Service
//public class TrafficDataSyncServiceImpl implements TrafficDataSyncService {
//
//    private static final int BATCH = 1000;
//    private long waterMark = 0L;
//
//    @Autowired
//    private TrafficDataMapper trafficDataMapper;
//    @Autowired
//    private TobCacheMapper cacheMapper;
//    @Autowired
//    private TobSuspicionLevelMapper tobSuspicionLevelMapper;
//
//    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//
//    @PostConstruct
//    public void init() {
//        System.out.println("【DEBUG】PostConstruct init() 开始执行");
//        TobCacheDaySlice slice = cacheMapper.selectDaySliceOne();
//        System.out.println("【DEBUG】slice = " + slice);
//        System.out.println("【DEBUG】maxTrafficId = " +
//                (slice == null ? "null" : slice.getMaxTrafficId()));
//        waterMark = slice == null || slice.getMaxTrafficId() == null
//                ? 0L : slice.getMaxTrafficId();
//        System.out.println("【DEBUG】计算后 waterMark = " + waterMark);
//
//        TobSuspicionLevel levelPo = tobSuspicionLevelMapper.selectSuspicionLevel();
//        System.out.println("【DEBUG】TobSuspicionLevelMapper.selectSuspicionLevel() 结果 = " + levelPo);
//        if (levelPo == null) {
//            System.out.println("【DEBUG】库里的嫌疑阈值是 null，插入默认值 0.0");
//            tobSuspicionLevelMapper.insertSuspicionLevel(0.0);
//        } else {
//            System.out.println("【DEBUG】库里已有嫌疑阈值，不再插入");
//        }
//        System.out.println("【DEBUG】PostConstruct init() 执行完毕");
//    }
//
//    @Override
//    @Transactional
//    public void sync() {
//        System.out.println("【DEBUG】sync() 开始，当前 waterMark = " + waterMark);
//        List<TrafficData> news = trafficDataMapper.fetchNewById(waterMark, BATCH);
//        System.out.println("【DEBUG】trafficDataMapper.fetchNewById 结果条数 = " +
//                (news == null ? "null" : news.size()));
//        if (news == null || news.isEmpty()) {
//            System.out.println("【DEBUG】没有新数据，sync() 直接返回");
//            return;
//        }
//
//        long batchMaxId = waterMark;
//        String nowYM = sdf.format(new Date()).substring(0, 7);
//        System.out.println("【DEBUG】nowYM = " + nowYM);
//
//        TobSuspicionLevel po = tobSuspicionLevelMapper.selectSuspicionLevel();
//        System.out.println("【DEBUG】再次查询 TobSuspicionLevel = " + po);
//        double curLevel = 0.0;
//        if (po != null && po.getLevel() != null) {
//            curLevel = po.getLevel();
//        }
//        System.out.println("【DEBUG】最终 curLevel = " + curLevel);
//
//        int index = 0;
//        for (TrafficData t : news) {
//            index++;
//            System.out.println("【DEBUG】开始处理第 " + index + " 条数据，trafficData.id = " + t.getId());
//            System.out.println("【DEBUG】本条数据 level = " + t.getLevel() +
//                    " , curLevel = " + curLevel +
//                    " , 是否达标 = " + (t.getLevel() != null && t.getLevel() >= curLevel));
//
//            batchMaxId = Math.max(batchMaxId, t.getId());
//
//            TobCacheCamera camera = buildCamera(t, curLevel);
//            System.out.println("【DEBUG】buildCamera 结果 = " + camera);
//            TobCacheDaynight dayN = buildDaynight(t, curLevel);
//            System.out.println("【DEBUG】buildDaynight 结果 = " + dayN);
//            TobCacheDaySlice slice = buildDaySlice(t, batchMaxId, nowYM, curLevel);
//            System.out.println("【DEBUG】buildDaySlice 结果 = " + slice);
//            TobCacheHour hour = buildHour(t, curLevel);
//            System.out.println("【DEBUG】buildHour 结果 = " + hour);
//            TobCacheSeason season = buildSeason(t, curLevel);
//            System.out.println("【DEBUG】buildSeason 结果 = " + season);
//            TobCacheVehicleColor color = buildColor(t, curLevel);
//            System.out.println("【DEBUG】buildColor 结果 = " + color);
//            TobCacheVehicleType type = buildType(t, curLevel);
//            System.out.println("【DEBUG】buildType 结果 = " + type);
//
//            cacheMapper.incCamera(camera);
//            cacheMapper.incDaynight(dayN);
//            cacheMapper.incDaySlice(slice);
//            cacheMapper.incHour(hour);
//            cacheMapper.incSeason(season);
//            cacheMapper.incVehicleColor(color);
//            cacheMapper.incVehicleType(type);
//            System.out.println("【DEBUG】第 " + index + " 条数据 所有 inc* 方法已调用完毕");
//        }
//        waterMark = batchMaxId;
//        System.out.println("【DEBUG】本批处理结束，最终 waterMark 更新为 = " + waterMark);
//    }
//
//    /* ------------------- 构建：每条数据 new 一个新对象 ------------------- */
//
//    private TobCacheCamera buildCamera(TrafficData t, double level) {
//        System.out.println("【DEBUG】进入 buildCamera，cameraName = " + t.getCameraName() +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheCamera c = new TobCacheCamera();
//        if (t.getLevel() == null || t.getLevel() < level) {
//            System.out.println("【DEBUG】buildCamera 嫌疑不达标，直接返回空对象");
//            return c;
//        }
//        String name = t.getCameraName();
//        if (name == null) {
//            c.setOther(1L);
//            System.out.println("【DEBUG】buildCamera cameraName 为 null，走 other");
//            return c;
//        }
//        name = name.trim();
//        switch (name) {
//            case "板桥镇326国道中国石化加油站旁卡口":
//                c.setBanqiao326Gas(1L);
//                break;
//            case "老地方饭店附近卡口":
//                c.setLaoDifang(1L);
//                break;
//            case "G248国道与西响线交叉口南向卡口":
//                c.setG248Xixiang(1L);
//                break;
//            case "板桥收费站出口一体机":
//                c.setBanqiaoExit(1L);
//                break;
//            case "陆良县326国道经官中学附近卡口":
//                c.setLuliangKaikou(1L);
//                break;
//            case "罗平县阿岗收费站出口卡口":
//                c.setLuopingAgang(1L);
//                break;
//            default:
//                c.setOther(1L);
//                break;
//        }
//        System.out.println("【DEBUG】buildCamera 最终返回 = " + c);
//        return c;
//    }
//
//    private TobCacheDaynight buildDaynight(TrafficData t, double level) {
//        System.out.println("【DEBUG】进入 buildDaynight，captureTime = " + t.getCaptureTime() +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheDaynight n = new TobCacheDaynight();
//        if (t.getLevel() == null || t.getLevel() < level) {
//            System.out.println("【DEBUG】buildDaynight 嫌疑不达标，直接返回空对象");
//            return n;
//        }
//        int h = hourOf(t.getCaptureTime());
//        if (h >= 6 && h < 18) n.setDayNum(1L);
//        else n.setNightNum(1L);
//        System.out.println("【DEBUG】buildDaynight 计算后 hour = " + h + " , 结果 = " + n);
//        return n;
//    }
//
//    private TobCacheDaySlice buildDaySlice(TrafficData t, long currId, String nowYM, double level) {
//        System.out.println("【DEBUG】进入 buildDaySlice，captureTime = " + t.getCaptureTime() +
//                " , currId = " + currId + " , nowYM = " + nowYM +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheDaySlice s = new TobCacheDaySlice();
//        int days = daysBeforeToday(t.getCaptureTime());
//        System.out.println("【DEBUG】buildDaySlice daysBeforeToday = " + days);
//
//        if (days >= 0 && days <= 7) {
//            switch (days) {
//                case 0:
//                    s.setD0(1L);
//                    break;
//                case 1:
//                    s.setD1(1L);
//                    break;
//                case 2:
//                    s.setD2(1L);
//                    break;
//                case 3:
//                    s.setD3(1L);
//                    break;
//                case 4:
//                    s.setD4(1L);
//                    break;
//                case 5:
//                    s.setD5(1L);
//                    break;
//                case 6:
//                    s.setD6(1L);
//                    break;
//                case 7:
//                    s.setD7(1L);
//                    break;
//            }
//        }
//        if (t.getCaptureTime().substring(0, 7).equals(nowYM)) {
//            s.setMonthCurr(1L);
//        }
//
//        boolean sus = t.getLevel() != null && t.getLevel() >= level;
//        System.out.println("【DEBUG】buildDaySlice 嫌疑达标？ " + sus);
//        if (sus) {
//            if (days >= 0 && days <= 7) {
//                switch (days) {
//                    case 0:
//                        s.setSuspectD0(1L);
//                        break;
//                    case 1:
//                        s.setSuspectD1(1L);
//                        break;
//                    case 2:
//                        s.setSuspectD2(1L);
//                        break;
//                    case 3:
//                        s.setSuspectD3(1L);
//                        break;
//                    case 4:
//                        s.setSuspectD4(1L);
//                        break;
//                    case 5:
//                        s.setSuspectD5(1L);
//                        break;
//                    case 6:
//                        s.setSuspectD6(1L);
//                        break;
//                    case 7:
//                        s.setSuspectD7(1L);
//                        break;
//                }
//            }
//            if (t.getCaptureTime().substring(0, 7).equals(nowYM)) {
//                s.setSuspectMonthCurr(1L);
//            }
//        }
//        s.setMaxTrafficId(currId);
//        System.out.println("【DEBUG】buildDaySlice 最终返回 = " + s);
//        return s;
//    }
//
//    private TobCacheHour buildHour(TrafficData t, double level) {
//        System.out.println("【DEBUG】进入 buildHour，captureTime = " + t.getCaptureTime() +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheHour h = new TobCacheHour();
//        if (t.getLevel() == null || t.getLevel() < level) {
//            System.out.println("【DEBUG】buildHour 嫌疑不达标，直接返回空对象");
//            return h;
//        }
//        int hour = hourOf(t.getCaptureTime());
//        System.out.println("【DEBUG】buildHour 计算得 hour = " + hour);
//        switch (hour) {
//            case 0:
//                h.setH00(1L);
//                break;
//            case 1:
//                h.setH01(1L);
//                break;
//            case 2:
//                h.setH02(1L);
//                break;
//            case 3:
//                h.setH03(1L);
//                break;
//            case 4:
//                h.setH04(1L);
//                break;
//            case 5:
//                h.setH05(1L);
//                break;
//            case 6:
//                h.setH06(1L);
//                break;
//            case 7:
//                h.setH07(1L);
//                break;
//            case 8:
//                h.setH08(1L);
//                break;
//            case 9:
//                h.setH09(1L);
//                break;
//            case 10:
//                h.setH10(1L);
//                break;
//            case 11:
//                h.setH11(1L);
//                break;
//            case 12:
//                h.setH12(1L);
//                break;
//            case 13:
//                h.setH13(1L);
//                break;
//            case 14:
//                h.setH14(1L);
//                break;
//            case 15:
//                h.setH15(1L);
//                break;
//            case 16:
//                h.setH16(1L);
//                break;
//            case 17:
//                h.setH17(1L);
//                break;
//            case 18:
//                h.setH18(1L);
//                break;
//            case 19:
//                h.setH19(1L);
//                break;
//            case 20:
//                h.setH20(1L);
//                break;
//            case 21:
//                h.setH21(1L);
//                break;
//            case 22:
//                h.setH22(1L);
//                break;
//            case 23:
//                h.setH23(1L);
//                break;
//            default:
//                break;
//        }
//        System.out.println("【DEBUG】buildHour 最终返回 = " + h);
//        return h;
//    }
//
//    private TobCacheSeason buildSeason(TrafficData t, double level) {
//        System.out.println("【DEBUG】进入 buildSeason，captureTime = " + t.getCaptureTime() +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheSeason s = new TobCacheSeason();
//        if (t.getLevel() == null || t.getLevel() < level) {
//            System.out.println("【DEBUG】buildSeason 嫌疑不达标，直接返回空对象");
//            return s;
//        }
//        int m = monthOf(t.getCaptureTime());
//        System.out.println("【DEBUG】buildSeason 计算得 month = " + m);
//        if (m >= 3 && m <= 5) {
//            s.setSpring(1L);
//        } else if (m >= 6 && m <= 8) {
//            s.setSummer(1L);
//        } else if (m >= 9 && m <= 11) {
//            s.setAutumn(1L);
//        } else {
//            s.setWinter(1L);
//        }
//        System.out.println("【DEBUG】buildSeason 最终返回 = " + s);
//        return s;
//    }
//
//    private TobCacheVehicleColor buildColor(TrafficData t, double level) {
//        System.out.println("【DEBUG】进入 buildColor，vehicleColor = " + t.getVehicleColor() +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheVehicleColor c = new TobCacheVehicleColor();
//        if (t.getLevel() == null || t.getLevel() < level) {
//            System.out.println("【DEBUG】buildColor 嫌疑不达标，直接返回空对象");
//            return c;
//        }
//        String v = t.getVehicleColor();
//        if (v == null) {
//            c.setOtherC(1L);
//            System.out.println("【DEBUG】buildColor vehicleColor 为 null，走 otherC");
//            return c;
//        }
//        if (v.contains("白色")) {
//            c.setWhite(1L);
//        } else if (v.contains("黑色")) {
//            c.setBlack(1L);
//        } else if (v.contains("灰色")) {
//            c.setGray(1L);
//        } else if (v.contains("红色")) {
//            c.setRed(1L);
//        } else if (v.contains("蓝色") && !v.contains("深蓝色")) {
//            c.setBlue(1L);
//        } else if (v.contains("棕色") || v.contains("褐色")) {
//            c.setBrown(1L);
//        } else if (v.contains("银色")) {
//            c.setSilver(1L);
//        } else if (v.contains("黄色")) {
//            c.setYellow(1L);
//        } else if (v.contains("绿色")) {
//            c.setGreen(1L);
//        } else if (v.contains("深蓝色")) {
//            c.setDeepBlue(1L);
//        } else {
//            c.setOtherC(1L);
//        }
//        System.out.println("【DEBUG】buildColor 最终返回 = " + c);
//        return c;
//    }
//
//    private TobCacheVehicleType buildType(TrafficData t, double level) {
//        System.out.println("【DEBUG】进入 buildType，vehicleType = " + t.getVehicleType() +
//                " , level = " + t.getLevel() + " , curLevel = " + level);
//        TobCacheVehicleType v = new TobCacheVehicleType();
//        if (t.getLevel() == null || t.getLevel() < level) {
//            System.out.println("【DEBUG】buildType 嫌疑不达标，直接返回空对象");
//            return v;
//        }
//        String type = t.getVehicleType();
//        if (type == null) {
//            v.setOther(1L);
//            System.out.println("【DEBUG】buildType vehicleType 为 null，走 other");
//            return v;
//        }
//        type = type.trim();
//        System.out.println("【DEBUG】buildType trim 后 vehicleType = " + type);
//        switch (type) {
//            case "轿车":
//                v.setSedan(1L);
//                break;
//            case "SUV/MPV":
//                v.setSuvMpv(1L);
//                break;
//            case "货车":
//                v.setTruck(1L);
//                break;
//            case "二轮车":
//                v.setTwoWheeler(1L);
//                break;
//            case "面包车":
//                v.setVan(1L);
//                break;
//            case "小货车":
//                v.setLightTruck(1L);
//                break;
//            case "三轮车":
//                v.setThreeWheeler(1L);
//                break;
//            case "小型车":
//                v.setSmallCar(1L);
//                break;
//            case "行人":
//                v.setPedestrian(1L);
//                break;
//            case "大型车":
//                v.setLargeVehicle(1L);
//                break;
//            case "皮卡车":
//                v.setPickup(1L);
//                break;
//            case "大型客车":
//                v.setLargeBus(1L);
//                break;
//            case "未知":
//                v.setUnknown(1L);
//                break;
//            case "非机动车":
//                v.setNonMotor(1L);
//                break;
//            case "中型客车":
//                v.setMediumBus(1L);
//                break;
//            case "suv":
//                v.setSuvOnly(1L);
//                break;
//            default:
//                v.setOther(1L);
//                break;
//        }
//        System.out.println("【DEBUG】buildType 最终返回 = " + v);
//        return v;
//    }
//
//    /* ------------------- 时间工具（JDK8 兼容） ------------------- */
//
//    private int hourOf(String dateStr) {
//        try {
//            Date d = sdf.parse(dateStr);
//            return d.getHours();
//        } catch (ParseException e) {
//            System.out.println("【DEBUG】hourOf 解析异常，dateStr = " + dateStr + " 返回 0");
//            return 0;
//        }
//    }
//
//    private int dayOfMonth(String dateStr) {
//        try {
//            Date d = sdf.parse(dateStr);
//            return d.getDate();
//        } catch (ParseException e) {
//            System.out.println("【DEBUG】dayOfMonth 解析异常，dateStr = " + dateStr + " 返回 0");
//            return 0;
//        }
//    }
//
//    private int monthOf(String dateStr) {
//        try {
//            Date d = sdf.parse(dateStr);
//            return d.getMonth() + 1;
//        } catch (ParseException e) {
//            System.out.println("【DEBUG】monthOf 解析异常，dateStr = " + dateStr + " 返回 1");
//            return 1;
//        }
//    }
//
//    private int daysBeforeToday(String captureTime) {
//        try {
//            Date cap = sdf.parse(captureTime);
//            Date today = new Date();
//            today = sdf.parse(sdf.format(today));
//            long diff = today.getTime() - cap.getTime();
//            int days = (int) (diff / (24 * 60 * 60 * 1000));
//            int result = days >= 0 && days <= 7 ? days : -1;
//            System.out.println("【DEBUG】daysBeforeToday captureTime = " + captureTime +
//                    " , 计算得 days = " + result);
//            return result;
//        } catch (ParseException e) {
//            System.out.println("【DEBUG】daysBeforeToday 解析异常，captureTime = " + captureTime + " 返回 -1");
//            return -1;
//        }
//    }
//
//    @Scheduled(cron = "0 0 0 * * ?")
//    public void dayShiftJob() {
//        System.out.println("【DEBUG】dayShiftJob 触发");
//        cacheMapper.dayShift();
//        System.out.println("【DEBUG】dayShiftJob 执行完毕");
//    }
//
//    @Scheduled(cron = "0 0 0 1 * ?")
//    public void monthResetJob() {
//        System.out.println("【DEBUG】monthResetJob 触发");
//        cacheMapper.monthReset();
//        System.out.println("【DEBUG】monthResetJob 执行完毕");
//    }
//}