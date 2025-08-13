package com.ruoyi.system.service.impl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.TobPerson;
import com.ruoyi.system.domain.TobVehicle;
import com.ruoyi.system.domain.undefine.Duration;
import com.ruoyi.system.domain.undefine.VehicleBehaviorColor;
import com.ruoyi.system.domain.undefine.VehicleType;
import com.ruoyi.system.domain.vo.TobVehicleBehaviorVo;
import com.ruoyi.system.mapper.TobPersonMapper;
import com.ruoyi.system.mapper.TobVehicleMapper;
import com.ruoyi.system.service.ITobVehicleService;
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
     * 查询车辆类型列表
     *
     * @param tobVehicleBehaviorVo 查询条件对象，用于构建SQL查询语句的条件
     * @return 返回一个List集合，包含查询到的车辆类型统计分析数据
     */
    @Override
    public List<VehicleType> getVehicleTypeData(TobVehicleBehaviorVo tobVehicleBehaviorVo) {

        List<TobVehicleBehaviorVo> lists=tobVehicleBehaviorMapper.selectTobVehicleBehaviorVoList(tobVehicleBehaviorVo);
        Map<String, Integer> carTypeCountMap = new HashMap<>();
        List<VehicleType> vt=new ArrayList<>();
        for(TobVehicleBehaviorVo vo:lists){
            String carTypeId = vo.getCarTypeId().toString();
            carTypeCountMap.put(carTypeId, carTypeCountMap.getOrDefault(carTypeId, 0) + 1);
        }
        for (Map.Entry<String, Integer> entry : carTypeCountMap.entrySet()) {
            System.out.println(entry.getKey()+"+"+entry.getValue());
            vt.add(new VehicleType( entry.getValue(),entry.getKey()));
        }
        return vt;
    }

    /**
     * 查询车辆颜色数据
     *
     * @param tobVehicleBehaviorVo 车辆行为记录
     * @return 车辆行为记录集合
     */
    public VehicleBehaviorColor getVehicleColorData(TobVehicleBehaviorVo tobVehicleBehaviorVo ){
        List<TobVehicleBehaviorVo> lists=tobVehicleBehaviorMapper.selectTobVehicleBehaviorVoList(tobVehicleBehaviorVo);
        Map<String, Integer> carColorCountMap = new HashMap<>();
        for(TobVehicleBehaviorVo vo:lists){
            carColorCountMap.put(vo.getCarColor(), carColorCountMap.getOrDefault(vo.getCarColor(), 0) + 1);
        }

        return new VehicleBehaviorColor(new ArrayList<>(carColorCountMap.keySet()),new ArrayList<>(carColorCountMap.values()));
    }







    /**
     * 新增车辆行为记录
     * 
     * @param tobVehicleBehavior 车辆行为记录
     * @return 结果
     */
    @Override
    public int insertTobVehicleBehavior(TobVehicleBehavior tobVehicleBehavior) {
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

        // 插入新的车辆行为记录
        return tobVehicleBehaviorMapper.insertTobVehicleBehavior(tobVehicleBehavior);
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

    /**
     * 获取超过嫌疑最新前十条数据
     *
     * @param tobVehicleBehaviorVo 车辆行为记录
     * @return 结果
     */
    public List<TobVehicleBehaviorVo> getUpToDataDegreeSuspicion(TobVehicleBehaviorVo tobVehicleBehaviorVo){
        return tobVehicleBehaviorMapper.getUpToDataDegreeSuspicion(tobVehicleBehaviorVo);
    }

    /**
     * 获取超过嫌疑所有数据
     *
     * @param tobVehicleBehaviorVo 车辆行为记录
     * @return 结果
     */
    public List<TobVehicleBehaviorVo> getUpToDataDegreeSuspicionAll(TobVehicleBehaviorVo tobVehicleBehaviorVo){
        return tobVehicleBehaviorMapper.getUpToDataDegreeSuspicionAll(tobVehicleBehaviorVo);
    }

    /**
     * 获取制定日期的每日嫌疑车辆数据
     */
    public List<List<Object>> byDateGetSuspicionVehicleBehavior(Duration duration) {
        // 1. 获取原始数据
        List<TobVehicleBehaviorVo> records = tobVehicleBehaviorMapper.byDateGetSuspicionVehicleBehavior(duration);

        // 2. 准备返回结构
        List<Object> dateList = new ArrayList<>();
        List<Object> countList = new ArrayList<>();

        // 3. 统计每天的记录数（直接用List保持顺序）
        Map<String, Long> tempMap = new HashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("MM-dd");
        for (TobVehicleBehaviorVo record : records) {
            String dayKey = sdf.format(record.getCreateTime());
            tempMap.put(dayKey, tempMap.getOrDefault(dayKey, 0L) + 1);
        }

        // 4. 生成连续日期序列（严格按时间顺序）
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date startDate = inputFormat.parse(duration.getBegin());
            Date endDate = inputFormat.parse(duration.getEnd());

            Calendar cal = Calendar.getInstance();
            cal.setTime(startDate);

            while (!cal.getTime().after(endDate)) {
                String dayKey = sdf.format(cal.getTime());
                dateList.add(dayKey.replaceFirst("^0", "")); // 去掉前导零，例如 12-01 -> 12-1
                countList.add(tempMap.getOrDefault(dayKey, 0L));
                cal.add(Calendar.DATE, 1);
            }
        } catch (ParseException e) {
            throw new RuntimeException("日期解析错误", e);
        }

        return Arrays.asList(dateList, countList);
    }
}
