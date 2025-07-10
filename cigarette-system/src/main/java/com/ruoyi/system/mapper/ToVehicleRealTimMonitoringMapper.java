package com.ruoyi.system.mapper;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ToVehicleRealTimMonitoringMapper {
//    List<ToVehicleRealTimMonitoring> selectToVehicleRealTimMonitoringList(ToVehicleRealTimMonitoring query);
    ToVehicleRealTimMonitoring selectById(Long id);
    List<ToVehicleRealTimMonitoring> selectAll();
    ToVehicleRealTimMonitoring selectNext(@Param("lastId") Long lastId);

}
