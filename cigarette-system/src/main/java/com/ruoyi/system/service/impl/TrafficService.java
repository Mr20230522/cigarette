package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.CameraIdNameVO;
import com.ruoyi.system.mapper.TrafficDataMapper;
import com.ruoyi.system.service.ITrafficDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TrafficService implements ITrafficDataService {

    @Autowired
    private TrafficDataMapper trafficDataMapper;

    @Override
    public List<TrafficData> tenList(TrafficData trafficData){
        return trafficDataMapper.tenList(trafficData);
    }

    @Override
    public List<TrafficData> allList(TrafficData trafficData){
        return trafficDataMapper.allList(trafficData);
    }

    @Override
    public List<TrafficData> overIdList(TrafficData trafficData){
        return trafficDataMapper.overIdList(trafficData);
    }

    @Override
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData){
        return trafficDataMapper.byIdGetVideoPath(trafficData);
    }

    @Override
    public List<TrafficData> searchVehicle(SearchOfTheSuspectedVehicle search){
        return trafficDataMapper.searchVehicle(search);
    }

    @Override
    public List<CameraIdNameVO> getAllCameraIdNamePairs() {
        return trafficDataMapper.getAllCameraIdNamePairs();
    }
}