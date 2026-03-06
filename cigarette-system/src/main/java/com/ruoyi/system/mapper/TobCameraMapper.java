package com.ruoyi.system.mapper;

import java.util.List;
import com.ruoyi.system.domain.TobCamera;
import io.lettuce.core.dynamic.annotation.Param;

/**
 * 摄像头Mapper接口
 *
 * @author cigarette
 * @date 2024-07-28
 */
public interface TobCameraMapper
{
    /**
     * 查询摄像头
     *
     * @param cameraId 摄像头主键
     * @return 摄像头
     */
    public TobCamera selectTobCameraByCameraId(Long cameraId);

    /**
     * 查询摄像头列表
     *
     * @param tobCamera 摄像头
     * @return 摄像头集合
     */
    public List<TobCamera> selectTobCameraList(TobCamera tobCamera);

    /**
     * 新增摄像头
     *
     * @param tobCamera 摄像头
     * @return 结果
     */
    public int insertTobCamera(TobCamera tobCamera);

    /**
     * 修改摄像头
     *
     * @param tobCamera 摄像头
     * @return 结果
     */
    public int updateTobCamera(TobCamera tobCamera);

    /**
     * 删除摄像头
     *
     * @param cameraId 摄像头主键
     * @return 结果
     */
    public int deleteTobCameraByCameraId(Long cameraId);

    /**
     * 批量删除摄像头
     *
     * @param cameraIds 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTobCameraByCameraIds(Long[] cameraIds);

    /**
     * 根据地区数据查询摄像头列表
     * @param tobCamera
     * @return
     */
    List<TobCamera> selectTobCameraListByDistrictId(TobCamera tobCamera);


    // TobCameraMapper.java
    /**
     * 通过traffic_camera_id查询摄像头信息
     * @param trafficCameraId trafficdata表中的摄像头ID
     * @return 摄像头信息
     */
    TobCamera selectByTrafficCameraId(@Param("trafficCameraId") Long trafficCameraId);

}
