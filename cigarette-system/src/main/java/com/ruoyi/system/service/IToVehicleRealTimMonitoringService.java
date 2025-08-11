package com.ruoyi.system.service;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;

import java.util.Date;


public interface IToVehicleRealTimMonitoringService {
    //    List<ToVehicleRealTimMonitoring> selectToVehicleRealTimMonitoringList(ToVehicleRealTimMonitoring query);
    ToVehicleRealTimMonitoring getNextRecord(Long lastId);
    ToVehicleRealTimMonitoring getLastedRecord(Date captureTime);
}
