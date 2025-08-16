package com.ruoyi.web.controller.cigarette.trafficData;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.service.ITrafficDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cigarette/trafficData")
public class TrafficDataControl extends BaseController {

    @Autowired
    private ITrafficDataService TrafficDataService;

    /**
     * 查询车辆表前十条
     */
    @GetMapping("/tenList")
    public List<TrafficData> tenList(TrafficData trafficData){
        return TrafficDataService.tenList(trafficData);
    }


    /**
     * 查询车辆表前十条
     */
    @GetMapping("/allList")
    public List<TrafficData> allList( TrafficData trafficData){
        return TrafficDataService.allList(trafficData);
    }


    /**
     * 查询车辆表前十条
     */
    @GetMapping("/overIdList")
    public List<TrafficData> overIdList(TrafficData trafficData){
        System.out.println("control:!!!!!!!!trafficData"+trafficData.getLevel()+trafficData.getCaptureTime());
        return TrafficDataService.overIdList(trafficData);
    }


    /**
     * 通过id获取视屏地址
     */
    @GetMapping("/byIdGetVideoPath")
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData){
        return TrafficDataService.byIdGetVideoPath(trafficData);
    }
}
