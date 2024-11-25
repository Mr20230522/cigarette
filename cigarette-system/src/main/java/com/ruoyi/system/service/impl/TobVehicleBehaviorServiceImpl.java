package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.TobPerson;
import com.ruoyi.system.domain.TobVehicle;
import com.ruoyi.system.domain.vo.TobVehicleBehaviorVo;
import com.ruoyi.system.mapper.TobPersonMapper;
import com.ruoyi.system.mapper.TobVehicleMapper;
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
    @Autowired
    private TobVehicleMapper tobVehicleMapper;
    @Autowired
    private TobPersonMapper tobPersonMapper;
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
     * 查询车辆行为列表
     *
     * @param tobVehicleBehaviorVo 查询条件对象，用于构建SQL查询语句的条件
     * @return 返回一个List集合，包含查询到的车辆行为数据
     */
    @Override
    public List<TobVehicleBehaviorVo> selectTobVehicleBehaviorVoList(TobVehicleBehaviorVo tobVehicleBehaviorVo) {

        return tobVehicleBehaviorMapper.selectTobVehicleBehaviorVoList(tobVehicleBehaviorVo);
    }




    /**
     * 新增车辆行为记录
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 结果
     */
    @Override
    public Long insertTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior) {
        // 设置创建时间
        tobVehicleBehavior.setCreateTime(DateUtils.getNowDate());

        // 查询车辆信息
        TobVehicle vehicle = tobVehicleMapper.selectTobVehicleByCarId(tobVehicleBehavior.getCarId());

        // 确保车辆信息存在
        if (vehicle != null) {
            vehicle.setOccurrenceNumber(vehicle.getOccurrenceNumber() != null ? vehicle.getOccurrenceNumber() + 1 : 1);
            tobVehicleMapper.updateTobVehicle(vehicle);

        } else {
            // 处理车辆未找到的情况，可以抛出异常或返回错误信息
            throw new IllegalArgumentException("车辆ID对应的车辆未找到, ID: " + tobVehicleBehavior.getCarId());
        }

        // 查询人员信息
        TobPerson person = tobPersonMapper.selectTobPersonBySuspectId(tobVehicleBehavior.getDriverId());

        // 确保人员信息存在
        if (person != null) {
            person.setOccurrenceNumber(person.getOccurrenceNumber() != null ? person.getOccurrenceNumber() + 1 : 1);
            tobPersonMapper.updateTobPerson(person);

        }
        tobVehicleBehaviorMapper.insertTobVehicleBehavior(tobVehicleBehavior);

        return tobVehicleBehavior.getBehaviorId();

    }
//    public int insertTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior) {
//        tobVehicleBehavior.setCreateTime(DateUtils.getNowDate());
//
//        // 查询车辆信息
//        TobVehicle vehicle = tobVehicleMapper.selectTobVehicleByCarId(tobVehicleBehavior.getCarId());
//        if (vehicle != null) {
//            vehicle.setOccurrenceNumber(vehicle.getOccurrenceNumber() != null ? vehicle.getOccurrenceNumber() + 1 : 1);
//            tobVehicleMapper.updateTobVehicle(vehicle); // 更新车辆的出现次数
//        }
//
//        // 查询人员信息
//        TobPerson person = tobPersonMapper.selectTobPersonBySuspectId(tobVehicleBehavior.getCarId());
//        if (person != null) {
//            person.setOccurrenceNumber(person.getOccurrenceNumber() != null ? person.getOccurrenceNumber() + 1 : 1);
//            tobPersonMapper.updateTobPerson(person); // 更新人员的出现次数
//        }

        // 更新车辆行为记录
//        return tobVehicleBehaviorMapper.insertTobVehicleBehavior(tobVehicleBehavior);
//    }

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
