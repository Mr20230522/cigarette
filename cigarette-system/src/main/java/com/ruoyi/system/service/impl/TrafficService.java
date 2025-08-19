package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.mapper.ToVehicleRealTimMonitoringMapper;
import com.ruoyi.system.mapper.TrafficDataMapper;
import com.ruoyi.system.service.ITrafficDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;
@Service
public class TrafficService implements ITrafficDataService {
    @Autowired
    private TrafficDataMapper trafficDataMapper;

    //获取前十条数据
    @Override
    public List<TrafficData> tenList(TrafficData trafficData){
        return trafficDataMapper.tenList(trafficData);
    }

    //获取所有的数据
    @Override
    public List<TrafficData> allList(TrafficData trafficData){
        return trafficDataMapper.allList(trafficData);
    }

    //获取比id大的数据
    @Override
    public List<TrafficData> overIdList(TrafficData trafficData){
        return trafficDataMapper.overIdList(trafficData);
    }

    /**
     * 通过id获取视屏地址
     */
    @Override
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData){
        System.out.println("!@!@!@!@!@!@!@!@trafficData"+trafficData);
        List<TrafficData> tra=trafficDataMapper.byIdGetVideoPath(trafficData);
        System.out.println("tra))))))))))))))))))"+tra);
        return tra;
    }

}
