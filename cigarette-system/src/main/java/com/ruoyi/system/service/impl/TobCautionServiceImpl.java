package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobCautionMapper;
import com.ruoyi.system.domain.TobCaution;
import com.ruoyi.system.service.ITobCautionService;

/**
 * 预警记录Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@Service
public class TobCautionServiceImpl implements ITobCautionService 
{
    @Autowired
    private TobCautionMapper tobCautionMapper;

    /**
     * 查询预警记录
     * 
     * @param cautionId 预警记录主键
     * @return 预警记录
     */
    @Override
    public TobCaution selectTobCautionByCautionId(Long cautionId)
    {
        return tobCautionMapper.selectTobCautionByCautionId(cautionId);
    }

    /**
     * 查询预警记录列表
     * 
     * @param tobCaution 预警记录
     * @return 预警记录
     */
    @Override
    public List<TobCaution> selectTobCautionList(TobCaution tobCaution)
    {
        return tobCautionMapper.selectTobCautionList(tobCaution);
    }

    /**
     * 新增预警记录
     * 
     * @param tobCaution 预警记录
     * @return 结果
     */
    @Override
    public int insertTobCaution(TobCaution tobCaution)
    {
        tobCaution.setCreateTime(DateUtils.getNowDate());
        return tobCautionMapper.insertTobCaution(tobCaution);
    }

    /**
     * 修改预警记录
     * 
     * @param tobCaution 预警记录
     * @return 结果
     */
    @Override
    public int updateTobCaution(TobCaution tobCaution)
    {
        tobCaution.setUpdateTime(DateUtils.getNowDate());
        return tobCautionMapper.updateTobCaution(tobCaution);
    }

    /**
     * 批量删除预警记录
     * 
     * @param cautionIds 需要删除的预警记录主键
     * @return 结果
     */
    @Override
    public int deleteTobCautionByCautionIds(Long[] cautionIds)
    {
        return tobCautionMapper.deleteTobCautionByCautionIds(cautionIds);
    }

    /**
     * 删除预警记录信息
     * 
     * @param cautionId 预警记录主键
     * @return 结果
     */
    @Override
    public int deleteTobCautionByCautionId(Long cautionId)
    {
        return tobCautionMapper.deleteTobCautionByCautionId(cautionId);
    }
}
