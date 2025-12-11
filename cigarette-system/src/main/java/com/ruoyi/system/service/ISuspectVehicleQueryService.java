package com.ruoyi.system.service;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.SuspectVehicleQueryVO;

import java.util.List;

public interface ISuspectVehicleQueryService {
    List<ToVehicleRealTimMonitoring> selectSuspectVehicleList(SuspectVehicleQueryVO query);
}