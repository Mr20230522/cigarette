package com.ruoyi.system.service;

import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.CameraIdNameVO;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

public interface ITrafficDataService {

    //获取前十条数据
    public List<TrafficData> tenList(TrafficData trafficData);

    //获取所有的数据
    public List<TrafficData> allList(TrafficData trafficData);

    //获取比id大的数据
    public List<TrafficData> overIdList(TrafficData trafficData);


    /**
     * 通过id获取视屏地址
     */
    public List<TrafficData> byIdGetVideoPath(TrafficData trafficData);

    /**
     * 通过嫌疑查询条件获取视屏地址
     */
    public List<TrafficData> searchVehicle(SearchOfTheSuspectedVehicle search);

    /**
     * 获取所有摄像头ID和名称的配对数据
     * @return 摄像头ID和名称的配对列表
     */
    public List<CameraIdNameVO> getAllCameraIdNamePairs();
}
