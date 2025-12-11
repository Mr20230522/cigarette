
package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.SuspectVehicleQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SuspectVehicleQueryMapper {
    List<ToVehicleRealTimMonitoring> selectSuspectVehicleList(@Param("query") SuspectVehicleQueryVO query);
}