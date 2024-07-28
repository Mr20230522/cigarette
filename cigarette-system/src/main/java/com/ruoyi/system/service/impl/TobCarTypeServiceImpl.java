package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobCarTypeMapper;
import com.ruoyi.system.domain.TobCarType;
import com.ruoyi.system.service.ITobCarTypeService;

/**
 * 车型Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@Service
public class TobCarTypeServiceImpl implements ITobCarTypeService 
{
    @Autowired
    private TobCarTypeMapper tobCarTypeMapper;

    /**
     * 查询车型
     * 
     * @param carTypeId 车型主键
     * @return 车型
     */
    @Override
    public TobCarType selectTobCarTypeByCarTypeId(Long carTypeId)
    {
        return tobCarTypeMapper.selectTobCarTypeByCarTypeId(carTypeId);
    }

    /**
     * 查询车型列表
     * 
     * @param tobCarType 车型
     * @return 车型
     */
    @Override
    public List<TobCarType> selectTobCarTypeList(TobCarType tobCarType)
    {
        return tobCarTypeMapper.selectTobCarTypeList(tobCarType);
    }

    /**
     * 新增车型
     * 
     * @param tobCarType 车型
     * @return 结果
     */
    @Override
    public int insertTobCarType(TobCarType tobCarType)
    {
        tobCarType.setCreateTime(DateUtils.getNowDate());
        return tobCarTypeMapper.insertTobCarType(tobCarType);
    }

    /**
     * 修改车型
     * 
     * @param tobCarType 车型
     * @return 结果
     */
    @Override
    public int updateTobCarType(TobCarType tobCarType)
    {
        tobCarType.setUpdateTime(DateUtils.getNowDate());
        return tobCarTypeMapper.updateTobCarType(tobCarType);
    }

    /**
     * 批量删除车型
     * 
     * @param carTypeIds 需要删除的车型主键
     * @return 结果
     */
    @Override
    public int deleteTobCarTypeByCarTypeIds(Long[] carTypeIds)
    {
        return tobCarTypeMapper.deleteTobCarTypeByCarTypeIds(carTypeIds);
    }

    /**
     * 删除车型信息
     * 
     * @param carTypeId 车型主键
     * @return 结果
     */
    @Override
    public int deleteTobCarTypeByCarTypeId(Long carTypeId)
    {
        return tobCarTypeMapper.deleteTobCarTypeByCarTypeId(carTypeId);
    }
}
