package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.SuspectVehicleQueryVO;
import com.ruoyi.system.mapper.SuspectVehicleQueryMapper;
import com.ruoyi.system.service.ISuspectVehicleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SuspectVehicleQueryServiceImpl implements ISuspectVehicleQueryService {

    @Autowired
    private SuspectVehicleQueryMapper suspectVehicleQueryMapper;

    @Override
    public List<ToVehicleRealTimMonitoring> selectSuspectVehicleList(SuspectVehicleQueryVO query) {
        return suspectVehicleQueryMapper.selectSuspectVehicleList(query);
    }
}