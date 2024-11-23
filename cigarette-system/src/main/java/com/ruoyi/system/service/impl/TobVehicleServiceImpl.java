package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.vo.TobVehicleVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobVehicleMapper;
import com.ruoyi.system.domain.TobVehicle;
import com.ruoyi.system.service.ITobVehicleService;

/**
 * 车辆Service业务层处理
 *
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobVehicleServiceImpl implements ITobVehicleService
{
    @Autowired
    private TobVehicleMapper tobVehicleMapper;

    /**
     * 查询车辆
     *
     * @param carId 车辆主键
     * @return 车辆
     */
    @Override
    public TobVehicleVo selectTobVehicleByCarId(Long carId)
    {
        return tobVehicleMapper.selectTobVehicleByCarId(carId);
    }

    /**
     * 查询车辆列表
     *
     * @param tobVehicleVo 车辆
     * @return 车辆
     */
    @Override
    public List<TobVehicleVo> selectTobVehicleList(TobVehicleVo tobVehicleVo)
    {
        return tobVehicleMapper.selectTobVehicleList(tobVehicleVo);
    }


    /**
     * 新增车辆
     *
     * @param tobVehicle 车辆
     * @return 结果
     */
    @Override
    public int insertTobVehicle(TobVehicle tobVehicle)
    {
        tobVehicle.setCreateTime(DateUtils.getNowDate());
        tobVehicleMapper.insertTobVehicleInsert(tobVehicle);
        return tobVehicleMapper.insertTobVehicle(tobVehicle);
    }

    /**
     * 修改车辆
     *
     * @param tobVehicle 车辆
     * @return 结果
     */
    @Override
    public int updateTobVehicle(TobVehicle tobVehicle)
    {
        tobVehicle.setUpdateTime(DateUtils.getNowDate());
        return tobVehicleMapper.updateTobVehicle(tobVehicle);
    }

    /**
     * 批量删除车辆
     *
     * @param carIds 需要删除的车辆主键
     * @return 结果
     */
    @Override
    public int deleteTobVehicleByCarIds(Long[] carIds)
    {
        return tobVehicleMapper.deleteTobVehicleByCarIds(carIds);
    }

    /**
     * 删除车辆信息
     *
     * @param carId 车辆主键
     * @return 结果
     */
    @Override
    public int deleteTobVehicleByCarId(Long carId)
    {
        return tobVehicleMapper.deleteTobVehicleByCarId(carId);
    }
}
