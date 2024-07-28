package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobCaution;

/**
 * 预警记录Mapper接口
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public interface TobCautionMapper 
{
    /**
     * 查询预警记录
     * 
     * @param cautionId 预警记录主键
     * @return 预警记录
     */
    public TobCaution selectTobCautionByCautionId(Long cautionId);

    /**
     * 查询预警记录列表
     * 
     * @param tobCaution 预警记录
     * @return 预警记录集合
     */
    public List<TobCaution> selectTobCautionList(TobCaution tobCaution);

    /**
     * 新增预警记录
     * 
     * @param tobCaution 预警记录
     * @return 结果
     */
    public int insertTobCaution(TobCaution tobCaution);

    /**
     * 修改预警记录
     * 
     * @param tobCaution 预警记录
     * @return 结果
     */
    public int updateTobCaution(TobCaution tobCaution);

    /**
     * 删除预警记录
     * 
     * @param cautionId 预警记录主键
     * @return 结果
     */
    public int deleteTobCautionByCautionId(Long cautionId);

    /**
     * 批量删除预警记录
     * 
     * @param cautionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobCautionByCautionIds(Long[] cautionIds);
}
