package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobVehicleBehavior;

/**
 * 车辆行为记录Service接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface ITobVehicleBehaviorService 
{
    /**
     * 查询车辆行为记录
     * 
     * @param behaviorId 车辆行为记录主键
     * @return 车辆行为记录
     */
    public TobVehicleBehavior selectTobVehicleBehaviorByBehaviorId(Long behaviorId);

    /**
     * 查询车辆行为记录列表
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 车辆行为记录集合
     */
    public List<TobVehicleBehavior> selectTobVehicleBehaviorList(TobVehicleBehavior tobVehicleBehavior);

    /**
     * 新增车辆行为记录
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 结果
     */
    public int insertTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior);

    /**
     * 修改车辆行为记录
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 结果
     */
    public int updateTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior);

    /**
     * 批量删除车辆行为记录
     * 
     * @param behaviorIds 需要删除的车辆行为记录主键集合
     * @return 结果
     */
    public int deleteTobVehicleBehaviorByBehaviorIds(Long[] behaviorIds);

    /**
     * 删除车辆行为记录信息
     * 
     * @param behaviorId 车辆行为记录主键
     * @return 结果
     */
    public int deleteTobVehicleBehaviorByBehaviorId(Long behaviorId);
}
