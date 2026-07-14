package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TrafficData;
import com.ruoyi.system.domain.undefine.SearchOfTheSuspectedVehicle;
import com.ruoyi.system.domain.vo.CameraIdNameVO;
import org.apache.ibatis.annotations.Param;
import java.util.List;

public interface TrafficDataMapper {
    //获取前十条嫌疑数据
    public List<TrafficData> tenList(TrafficData trafficData);

    //获取当前查询十条嫌疑数据
    public List<TrafficData> nextTenList(TrafficData trafficData);

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

    /* 新增：增量同步专用 */
    List<TrafficData> fetchNewById(@Param("lastId") Long lastId, @Param("limit") int limit);

    /**
     * 根据主键Id查询交通数据
     */
    TrafficData selectById(@Param("id") Long id);

    /**
     * 获取所有摄像头ID和名称（去重）
     */
    List<CameraIdNameVO> getAllCameraIdNamePairs();
}
