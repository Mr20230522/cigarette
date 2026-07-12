package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobCameraRegion;
import java.util.List;

/**
 * 卡口地域映射Service接口
 */
public interface ITobCameraRegionService {

    /**
     * 查询卡口地域映射
     */
    TobCameraRegion selectTobCameraRegionById(Long id);

    /**
     * 根据卡口ID查询映射
     */
    TobCameraRegion selectTobCameraRegionByCameraId(Integer cameraId);

    /**
     * 查询卡口地域映射列表
     */
    List<TobCameraRegion> selectTobCameraRegionList(TobCameraRegion tobCameraRegion);

    /**
     * 新增卡口地域映射
     */
    int insertTobCameraRegion(TobCameraRegion tobCameraRegion);

    /**
     * 修改卡口地域映射
     */
    int updateTobCameraRegion(TobCameraRegion tobCameraRegion);

    /**
     * 删除卡口地域映射
     */
    int deleteTobCameraRegionByIds(Long[] ids);

    /**
     * 查询所有已生效的地域（DISTINCT，用于下拉选项）
     */
    List<TobCameraRegion> selectDistinctLocations();
}
