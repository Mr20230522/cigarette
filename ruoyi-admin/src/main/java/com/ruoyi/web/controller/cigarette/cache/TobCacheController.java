package com.ruoyi.web.controller.cigarette.cache;

import com.ruoyi.system.domain.cache.*;
import com.ruoyi.system.service.ITobCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
@RestController
@RequestMapping("/cigarette/cache/")
public class TobCacheController {
    @Autowired
    private ITobCacheService tobCacheService;
    @GetMapping("/selectCameraOne")
    public TobCacheCamera selectCameraOne() {
        return tobCacheService.selectCameraOne();
    }

    @GetMapping("/incCamera")
    public int incCamera(TobCacheCamera camera) {
        return tobCacheService.incCamera(camera);
    }

    @GetMapping("/selectDaynightOne")
    public TobCacheDaynight selectDaynightOne() {
        return tobCacheService.selectDaynightOne();
    }

    @GetMapping("/incDaynight")
    public int incDaynight(TobCacheDaynight daynight) {
        return tobCacheService.incDaynight(daynight);
    }

    @GetMapping("/selectDaySliceOne")
    public TobCacheDaySlice selectDaySliceOne() {
        return tobCacheService.selectDaySliceOne();
    }

    @GetMapping("/incDaySlice")
    public int incDaySlice(TobCacheDaySlice daySlice) {
        return tobCacheService.incDaySlice(daySlice);
    }

    @GetMapping("/selectHourOne")
    public TobCacheHour selectHourOne() {
        return tobCacheService.selectHourOne();
    }

    @GetMapping("/incHour")
    public int incHour(TobCacheHour hour) {
        return tobCacheService.incHour(hour);
    }

    @GetMapping("/selectSeasonOne")
    public TobCacheSeason selectSeasonOne() {
        return tobCacheService.selectSeasonOne();
    }

    @GetMapping("/incSeason")
    public int incSeason(TobCacheSeason season) {
        return tobCacheService.incSeason(season);
    }

    @GetMapping("/selectVehicleColorOne")
    public TobCacheVehicleColor selectVehicleColorOne() {
        return tobCacheService.selectVehicleColorOne();
    }

    @GetMapping("/incVehicleColor")
    public int incVehicleColor(TobCacheVehicleColor color) {
        return tobCacheService.incVehicleColor(color);
    }

    @GetMapping("/selectVehicleTypeOne")
    public TobCacheVehicleType selectVehicleTypeOne() {
        return tobCacheService.selectVehicleTypeOne();
    }

    @GetMapping("/incVehicleType")
    public int incVehicleType(TobCacheVehicleType type) {
        return tobCacheService.incVehicleType(type);
    }

}
