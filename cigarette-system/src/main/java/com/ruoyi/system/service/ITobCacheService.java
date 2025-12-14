package com.ruoyi.system.service;

import com.ruoyi.system.domain.cache.*;

public interface ITobCacheService {
    /* ------- 1. 相机 ------- */
    TobCacheCamera selectCameraOne();
    int incCamera(TobCacheCamera camera);

    /* ------- 2. 昼夜 ------- */
    TobCacheDaynight selectDaynightOne();
    int incDaynight(TobCacheDaynight daynight);

    /* ------- 3. 日切片 ------- */
    TobCacheDaySlice selectDaySliceOne();
    int incDaySlice(TobCacheDaySlice daySlice);
    void dayShift();
    void monthReset();

    /* ------- 4. 小时 ------- */
    TobCacheHour selectHourOne();
    int incHour(TobCacheHour hour);

    /* ------- 5. 季节 ------- */
    TobCacheSeason selectSeasonOne();
    int incSeason(TobCacheSeason season);

    /* ------- 6. 车辆颜色 ------- */
    TobCacheVehicleColor selectVehicleColorOne();
    int incVehicleColor(TobCacheVehicleColor color);

    /* ------- 7. 车辆类型 ------- */
    TobCacheVehicleType selectVehicleTypeOne();
    int incVehicleType(TobCacheVehicleType type);
}
