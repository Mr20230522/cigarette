package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.cache.*;
import com.ruoyi.system.mapper.TobCacheMapper;
import com.ruoyi.system.service.ITobCacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TobCacheServiceImpl implements ITobCacheService {
    @Autowired
    private TobCacheMapper tobCacheMapper;

    @Override
    public TobCacheCamera selectCameraOne() {
        return tobCacheMapper.selectCameraOne();
    }

    @Override
    public int incCamera(TobCacheCamera camera) {
        return tobCacheMapper.incCamera(camera);
    }

    @Override
    public TobCacheDaynight selectDaynightOne() {
        return tobCacheMapper.selectDaynightOne();
    }

    @Override
    public int incDaynight(TobCacheDaynight daynight) {
        return tobCacheMapper.incDaynight(daynight);
    }

    @Override
    public TobCacheDaySlice selectDaySliceOne() {
        return tobCacheMapper.selectDaySliceOne();
    }

    @Override
    public int incDaySlice(TobCacheDaySlice daySlice) {
        return tobCacheMapper.incDaySlice(daySlice);
    }

    @Override
    public void dayShift() {

    }

    @Override
    public void monthReset() {

    }

    @Override
    public TobCacheHour selectHourOne() {
        return tobCacheMapper.selectHourOne();
    }

    @Override
    public int incHour(TobCacheHour hour) {
        return tobCacheMapper.incHour(hour);
    }

    @Override
    public TobCacheSeason selectSeasonOne() {
        return tobCacheMapper.selectSeasonOne();
    }

    @Override
    public int incSeason(TobCacheSeason season) {
        return tobCacheMapper.incSeason(season);
    }

    @Override
    public TobCacheVehicleColor selectVehicleColorOne() {
        return tobCacheMapper.selectVehicleColorOne();
    }

    @Override
    public int incVehicleColor(TobCacheVehicleColor color) {
        return tobCacheMapper.incVehicleColor(color);
    }

    @Override
    public TobCacheVehicleType selectVehicleTypeOne() {
        return tobCacheMapper.selectVehicleTypeOne();
    }

    @Override
    public int incVehicleType(TobCacheVehicleType type) {
        return tobCacheMapper.incVehicleType(type);
    }
}
