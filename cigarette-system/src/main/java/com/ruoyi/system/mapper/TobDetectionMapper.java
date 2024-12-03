package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobDetection;

/**
 * 监测区域Mapper接口
 *
 * @author ruoyi
 * @date 2024-07-24
 */
public interface TobDetectionMapper
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
     * 删除监测区域
     *
     * @param detectionId 监测区域主键
     * @return 结果
     */
    public int deleteTobDetectionByDetectionId(Long detectionId);

    /**
     * 批量删除监测区域
     *
     * @param detectionIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobDetectionByDetectionIds(Long[] detectionIds);

    /**
     * 根据地区数据查询监测区域列表
     * @param tobDetection
     * @return
     */
    List<TobDetection> selectTobDetectionListByDistrictId(TobDetection tobDetection);
}
