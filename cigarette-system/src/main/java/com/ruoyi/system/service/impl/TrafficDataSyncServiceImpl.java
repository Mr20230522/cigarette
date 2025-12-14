package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.cache.*;
import com.ruoyi.system.mapper.TobCacheMapper;
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

    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    @PostConstruct
    public void init() {
        TobCacheDaySlice slice = cacheMapper.selectDaySliceOne();
        System.out.println("【init】slice = " + slice);
        System.out.println("【init】maxTrafficId = " +
                (slice == null ? "null" : slice.getMaxTrafficId()));
        waterMark = slice == null || slice.getMaxTrafficId() == null
                ? 0L : slice.getMaxTrafficId();
    }

    @Override
    @Transactional
    public void sync() {
        List<TrafficData> news = trafficDataMapper.fetchNewById(waterMark, BATCH);
        if (news == null || news.isEmpty()) return;

        long batchMaxId = waterMark;
        String nowYM = sdf.format(new Date()).substring(0, 7); // 循环外只算一次
        for (TrafficData t : news) {
            batchMaxId = Math.max(batchMaxId, t.getId());

            TobCacheCamera camera = buildCamera(t);
            TobCacheDaynight dayN = buildDaynight(t);
            TobCacheDaySlice slice = buildDaySlice(t, batchMaxId, nowYM);
            TobCacheHour hour = buildHour(t);
            TobCacheSeason season = buildSeason(t);
            TobCacheVehicleColor color = buildColor(t);
            TobCacheVehicleType type = buildType(t);

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

    private TobCacheCamera buildCamera(TrafficData t) {
        TobCacheCamera c = new TobCacheCamera();
        String name = t.getCameraName();
        if (name == null) {          // 库 NULL
            c.setOther(1L);
            return c;
        }
        name = name.trim();          // 去空格

        // 完全相等匹配（大小写随意）
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
                c.setOther(1L);   // 任何未列出卡口
                break;
        }
        return c;
    }

    private TobCacheDaynight buildDaynight(TrafficData t) {
        TobCacheDaynight n = new TobCacheDaynight();
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
    private TobCacheDaySlice buildDaySlice(TrafficData t, long currId,String nowYM) {
        TobCacheDaySlice s = new TobCacheDaySlice();
        int days = daysBeforeToday(t.getCaptureTime());
        //超过七天的就扔了
        if (days < 0) {
            if (t.getCaptureTime().substring(0, 7).equals(nowYM)) {
                s.setMonthCurr(1L);
            }
            s.setMaxTrafficId(currId);
            return s;                      // 不写任何 dₙ
        }
        // 写对应列
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
        if (t.getCaptureTime().substring(0, 7).equals(nowYM)) {
            s.setMonthCurr(1L);
        }
        s.setMaxTrafficId(currId);
        return s;
    }

    private TobCacheHour buildHour(TrafficData t) {
        TobCacheHour h = new TobCacheHour();
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

    private TobCacheSeason buildSeason(TrafficData t) {
        TobCacheSeason s = new TobCacheSeason();
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

    private TobCacheVehicleColor buildColor(TrafficData t) {
        TobCacheVehicleColor c = new TobCacheVehicleColor();
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

    private TobCacheVehicleType buildType(TrafficData t) {
        TobCacheVehicleType v = new TobCacheVehicleType();
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