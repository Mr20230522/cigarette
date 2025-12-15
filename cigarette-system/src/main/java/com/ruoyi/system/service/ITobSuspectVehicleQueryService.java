package com.ruoyi.system.service;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.TobSuspectVehicleQueryVO;

import java.util.List;

public interface ITobSuspectVehicleQueryService {
    List<ToVehicleRealTimMonitoring> selectSuspectVehicleList(TobSuspectVehicleQueryVO query);
}