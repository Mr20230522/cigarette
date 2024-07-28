package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobDetection;

/**
 * 监测区域Service接口
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public interface ITobDetectionService 
{
    /**
     * 查询监测区域
     * 
     * @param detectionId 监测区域主键
     * @return 监测区域
     */
    public TobDetection selectTobDetectionByDetectionId(Long detectionId);

    /**
     * 查询监测区域列表
     * 
     * @param tobDetection 监测区域
     * @return 监测区域集合
     */
    public List<TobDetection> selectTobDetectionList(TobDetection tobDetection);

    /**
     * 新增监测区域
     * 
     * @param tobDetection 监测区域
     * @return 结果
     */
    public int insertTobDetection(TobDetection tobDetection);

    /**
     * 修改监测区域
     * 
     * @param tobDetection 监测区域
     * @return 结果
     */
    public int updateTobDetection(TobDetection tobDetection);

    /**
     * 批量删除监测区域
     * 
     * @param detectionIds 需要删除的监测区域主键集合
     * @return 结果
     */
    public int deleteTobDetectionByDetectionIds(Long[] detectionIds);

    /**
     * 删除监测区域信息
     * 
     * @param detectionId 监测区域主键
     * @return 结果
     */
    public int deleteTobDetectionByDetectionId(Long detectionId);
}
