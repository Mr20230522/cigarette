package com.ruoyi.system.service;

import java.util.List;
import com.ruoyi.system.domain.TobCamera;

/**
 * 摄像头Service接口
 *
 * @author cigarette
 * @date 2024-07-28
 */
public interface ITobCameraService
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
     * 批量删除摄像头
     *
     * @param cameraIds 需要删除的摄像头主键集合
     * @return 结果
     */
    public int deleteTobCameraByCameraIds(Long[] cameraIds);

    /**
     * 删除摄像头信息
     *
     * @param cameraId 摄像头主键
     * @return 结果
     */
    public int deleteTobCameraByCameraId(Long cameraId);

    /**
     * 根据地区数据查询摄像头列表
     * @param tobCamera
     * @return
     */
    List<TobCamera> selectTobCameraListByDistrictId(TobCamera tobCamera);
}
