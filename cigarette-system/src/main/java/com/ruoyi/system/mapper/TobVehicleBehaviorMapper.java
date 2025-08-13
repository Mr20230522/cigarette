package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobVehicleBehavior;
import com.ruoyi.system.domain.undefine.Duration;
import com.ruoyi.system.domain.vo.TobVehicleBehaviorVo;

/**
 * 车辆行为记录Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface TobVehicleBehaviorMapper 
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
     * 查询车辆行为记录列表
     *
     * @param tobVehicleBehaviorVo 车辆行为记录
     * @return 车辆行为记录集合
     */
    public List<TobVehicleBehaviorVo> selectTobVehicleBehaviorVoList(TobVehicleBehaviorVo tobVehicleBehaviorVo);

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
     * 删除车辆行为记录
     * 
     * @param behaviorId 车辆行为记录主键
     * @return 结果
     */
    public int deleteTobVehicleBehaviorByBehaviorId(Long behaviorId);

    /**
     * 批量删除车辆行为记录
     * 
     * @param behaviorIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobVehicleBehaviorByBehaviorIds(Long[] behaviorIds);


    /**
     * 获取超过嫌疑最新前十条数据
     *
     * @param tobVehicleBehaviorVo 车辆行为记录
     * @return 结果
     */
    public List<TobVehicleBehaviorVo> getUpToDataDegreeSuspicion(TobVehicleBehaviorVo tobVehicleBehaviorVo);


    /**
     * 获取超过嫌疑所有数据
     *
     * @param tobVehicleBehaviorVo 车辆行为记录
     * @return 结果
     */
    public List<TobVehicleBehaviorVo> getUpToDataDegreeSuspicionAll(TobVehicleBehaviorVo tobVehicleBehaviorVo);


    public List<TobVehicleBehaviorVo> byDateGetSuspicionVehicleBehavior(Duration duration);

}
