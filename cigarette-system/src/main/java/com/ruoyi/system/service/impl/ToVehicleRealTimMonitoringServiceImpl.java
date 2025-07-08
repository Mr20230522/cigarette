package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;
import com.ruoyi.system.service.IToVehicleRealTimMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToVehicleRealTimMonitoringServiceImpl implements IToVehicleRealTimMonitoringService {

    @Autowired
    private ToVehicleRealTimMonitoringMapper mapper;

    @Override
    public List<ToVehicleRealTimMonitoring> selectToVehicleRealTimMonitoringList(ToVehicleRealTimMonitoring query) {
        return mapper.selectToVehicleRealTimMonitoringList(query);
    }
}
