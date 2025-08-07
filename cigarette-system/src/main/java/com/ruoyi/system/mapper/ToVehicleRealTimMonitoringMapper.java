package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface ToVehicleRealTimMonitoringMapper {
    //    List<ToVehicleRealTimMonitoring> selectToVehicleRealTimMonitoringList(ToVehicleRealTimMonitoring query);
    ToVehicleRealTimMonitoring selectById(Long id);

    List<ToVehicleRealTimMonitoring> selectAll();

    ToVehicleRealTimMonitoring selectNext(@Param("lastId") Long lastId);

    List<ToVehicleRealTimMonitoring> selectAfterId(@Param("lastId") Long lastId, @Param("limit") int limit);

    int updateLevel(@Param("id") Long id, @Param("level") Double level);
    ToVehicleRealTimMonitoring selectByTime(@Param("captureTime") Date captureTime);
}
