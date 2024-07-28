package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobCarType;

/**
 * 车型Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public interface TobCarTypeMapper 
{
    /**
     * 查询车型
     * 
     * @param carTypeId 车型主键
     * @return 车型
     */
    public TobCarType selectTobCarTypeByCarTypeId(Long carTypeId);

    /**
     * 查询车型列表
     * 
     * @param tobCarType 车型
     * @return 车型集合
     */
    public List<TobCarType> selectTobCarTypeList(TobCarType tobCarType);

    /**
     * 新增车型
     * 
     * @param tobCarType 车型
     * @return 结果
     */
    public int insertTobCarType(TobCarType tobCarType);

    /**
     * 修改车型
     * 
     * @param tobCarType 车型
     * @return 结果
     */
    public int updateTobCarType(TobCarType tobCarType);

    /**
     * 删除车型
     * 
     * @param carTypeId 车型主键
     * @return 结果
     */
    public int deleteTobCarTypeByCarTypeId(Long carTypeId);

    /**
     * 批量删除车型
     * 
     * @param carTypeIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobCarTypeByCarTypeIds(Long[] carTypeIds);
}
