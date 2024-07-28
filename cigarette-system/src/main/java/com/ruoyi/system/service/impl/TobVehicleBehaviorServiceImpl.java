package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobVehicleBehaviorMapper;
import com.ruoyi.system.domain.TobVehicleBehavior;
import com.ruoyi.system.service.ITobVehicleBehaviorService;

/**
 * 车辆行为记录Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobVehicleBehaviorServiceImpl implements ITobVehicleBehaviorService 
{
    @Autowired
    private TobVehicleBehaviorMapper tobVehicleBehaviorMapper;

    /**
     * 查询车辆行为记录
     * 
     * @param behaviorId 车辆行为记录主键
     * @return 车辆行为记录
     */
    @Override
    public TobVehicleBehavior selectTobVehicleBehaviorByBehaviorId(Long behaviorId)
    {
        return tobVehicleBehaviorMapper.selectTobVehicleBehaviorByBehaviorId(behaviorId);
    }

    /**
     * 查询车辆行为记录列表
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 车辆行为记录
     */
    @Override
    public List<TobVehicleBehavior> selectTobVehicleBehaviorList(TobVehicleBehavior tobVehicleBehavior)
    {
        return tobVehicleBehaviorMapper.selectTobVehicleBehaviorList(tobVehicleBehavior);
    }

    /**
     * 新增车辆行为记录
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 结果
     */
    @Override
    public int insertTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior)
    {
        tobVehicleBehavior.setCreateTime(DateUtils.getNowDate());
        return tobVehicleBehaviorMapper.insertTobVehicleBehavior(tobVehicleBehavior);
    }

    /**
     * 修改车辆行为记录
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 结果
     */
    @Override
    public int updateTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior)
    {
        tobVehicleBehavior.setUpdateTime(DateUtils.getNowDate());
        return tobVehicleBehaviorMapper.updateTobVehicleBehavior(tobVehicleBehavior);
    }

    /**
     * 批量删除车辆行为记录
     * 
     * @param behaviorIds 需要删除的车辆行为记录主键
     * @return 结果
     */
    @Override
    public int deleteTobVehicleBehaviorByBehaviorIds(Long[] behaviorIds)
    {
        return tobVehicleBehaviorMapper.deleteTobVehicleBehaviorByBehaviorIds(behaviorIds);
    }

    /**
     * 删除车辆行为记录信息
     * 
     * @param behaviorId 车辆行为记录主键
     * @return 结果
     */
    @Override
    public int deleteTobVehicleBehaviorByBehaviorId(Long behaviorId)
    {
        return tobVehicleBehaviorMapper.deleteTobVehicleBehaviorByBehaviorId(behaviorId);
    }
}
