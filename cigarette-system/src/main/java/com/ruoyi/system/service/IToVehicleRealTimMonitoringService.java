package com.ruoyi.system.service;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;

public interface IToVehicleRealTimMonitoringService {
    //    List<ToVehicleRealTimMonitoring> selectToVehicleRealTimMonitoringList(ToVehicleRealTimMonitoring query);
    ToVehicleRealTimMonitoring getNextRecord(Long lastId);
}
