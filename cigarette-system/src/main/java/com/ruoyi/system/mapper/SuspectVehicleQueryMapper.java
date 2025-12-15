
package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.TobSuspectVehicleQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuspectVehicleQueryMapper {
    List<ToVehicleRealTimMonitoring> selectSuspectVehicleList(@Param("query") TobSuspectVehicleQueryVO query);
}