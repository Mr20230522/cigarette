package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.TobSuspectVehicleQueryVO;
import com.ruoyi.system.mapper.SuspectVehicleQueryMapper;
import com.ruoyi.system.service.ITobSuspectVehicleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TobSuspectVehicleQueryServiceImpl implements ITobSuspectVehicleQueryService {

    @Autowired
    private SuspectVehicleQueryMapper suspectVehicleQueryMapper;

    @Override
    public List<ToVehicleRealTimMonitoring> selectSuspectVehicleList(TobSuspectVehicleQueryVO query) {
        return suspectVehicleQueryMapper.selectSuspectVehicleList(query);
    }
}