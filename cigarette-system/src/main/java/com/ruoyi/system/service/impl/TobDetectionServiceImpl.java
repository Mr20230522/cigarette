package com.ruoyi.system.service.impl;

import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobDetectionMapper;
import com.ruoyi.system.domain.TobDetection;
import com.ruoyi.system.service.ITobDetectionService;

/**
 * 监测区域Service业务层处理
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@Service
public class TobDetectionServiceImpl implements ITobDetectionService 
{
    @Autowired
    private TobDetectionMapper tobDetectionMapper;

    /**
     * 查询监测区域
     * 
     * @param detectionId 监测区域主键
     * @return 监测区域
     */
    @Override
    public TobDetection selectTobDetectionByDetectionId(Long detectionId)
    {
        return tobDetectionMapper.selectTobDetectionByDetectionId(detectionId);
    }

    /**
     * 查询监测区域列表
     * 
     * @param tobDetection 监测区域
     * @return 监测区域
     */
    @Override
    public List<TobDetection> selectTobDetectionList(TobDetection tobDetection)
    {
        return tobDetectionMapper.selectTobDetectionList(tobDetection);
    }

    /**
     * 新增监测区域
     * 
     * @param tobDetection 监测区域
     * @return 结果
     */
    @Override
    public int insertTobDetection(TobDetection tobDetection)
    {
        tobDetection.setCreateTime(DateUtils.getNowDate());
        return tobDetectionMapper.insertTobDetection(tobDetection);
    }

    /**
     * 修改监测区域
     * 
     * @param tobDetection 监测区域
     * @return 结果
     */
    @Override
    public int updateTobDetection(TobDetection tobDetection)
    {
        tobDetection.setUpdateTime(DateUtils.getNowDate());
        return tobDetectionMapper.updateTobDetection(tobDetection);
    }

    /**
     * 批量删除监测区域
     * 
     * @param detectionIds 需要删除的监测区域主键
     * @return 结果
     */
    @Override
    public int deleteTobDetectionByDetectionIds(Long[] detectionIds)
    {
        return tobDetectionMapper.deleteTobDetectionByDetectionIds(detectionIds);
    }

    /**
     * 删除监测区域信息
     * 
     * @param detectionId 监测区域主键
     * @return 结果
     */
    @Override
    public int deleteTobDetectionByDetectionId(Long detectionId)
    {
        return tobDetectionMapper.deleteTobDetectionByDetectionId(detectionId);
    }
}
