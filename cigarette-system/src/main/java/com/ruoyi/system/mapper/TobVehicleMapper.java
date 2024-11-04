package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobVehicle;

/**
 * 车辆Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface TobVehicleMapper 
{
    /**
     * 查询车辆
     * 
     * @param carId 车辆主键
     * @return 车辆
     */
    public TobVehicle selectTobVehicleByCarId(Long carId);

    /**
     * 查询车辆列表
     * 
     * @param tobVehicle 车辆
     * @return 车辆集合
     */
    public List<TobVehicle> selectTobVehicleList(TobVehicle tobVehicle);

    /**
     * 查询车辆列表
     *
     * @param tobVehicle 新增车辆
     * @return 车辆集合
     */
    public List<TobVehicle> selectTobVehicleInsertList(TobVehicle tobVehicle);

    /**
     * 新增车辆
     * 
     * @param tobVehicle 车辆
     * @return 结果
     */
    public int insertTobVehicle(TobVehicle tobVehicle);


    /**
     * 未录入车辆（也就是新增车辆）
     * 只不过这里要插入另一张表里面
     *
     * @param tobVehicle 车辆
     * @return 结果
     */
    public int insertTobVehicleInsert(TobVehicle tobVehicle);

    /**
     * 修改车辆
     * 
     * @param tobVehicle 车辆
     * @return 结果
     */
    public int updateTobVehicle(TobVehicle tobVehicle);

    /**
     * 删除车辆
     * 
     * @param carId 车辆主键
     * @return 结果
     */
    public int deleteTobVehicleByCarId(Long carId);

    /**
     * 批量删除车辆
     * 
     * @param carIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobVehicleByCarIds(Long[] carIds);
}
