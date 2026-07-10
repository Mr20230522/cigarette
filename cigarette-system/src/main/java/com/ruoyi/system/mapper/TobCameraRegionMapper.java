package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobCameraRegion;
import org.apache.ibatis.annotations.Param;
import java.util.List;

/**
 * 卡口地域映射Mapper接口
 */
public interface TobCameraRegionMapper {

    /**
     * 查询卡口地域映射
     */
    TobCameraRegion selectTobCameraRegionById(Long id);

    /**
     * 根据卡口ID查询
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
    int deleteTobCameraRegionById(Long id);

    /**
     * 批量删除卡口地域映射
     */
    int deleteTobCameraRegionByIds(Long[] ids);

    /**
     * 根据地域ID查询（用于获取群Webhook配置）
     */
    TobCameraRegion selectTobCameraRegionByLocationId(@Param("locationId") Integer locationId);
}
