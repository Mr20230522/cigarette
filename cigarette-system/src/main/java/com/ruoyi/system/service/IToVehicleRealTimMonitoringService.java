package com.ruoyi.system.service;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;

import java.util.List;

public interface IToVehicleRealTimMonitoringService {
    //    List<ToVehicleRealTimMonitoring> selectToVehicleRealTimMonitoringList(ToVehicleRealTimMonitoring query);
    ToVehicleRealTimMonitoring getNextRecord(Long lastId);
}
