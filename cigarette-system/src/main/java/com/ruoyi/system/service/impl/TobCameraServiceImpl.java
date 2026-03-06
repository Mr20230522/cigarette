package com.ruoyi.system.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import com.ruoyi.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.ruoyi.system.mapper.TobCameraMapper;
import com.ruoyi.system.domain.TobCamera;
import com.ruoyi.system.service.ITobCameraService;

/**
 * 摄像头Service业务层处理
 *
 * @author cigarette
 * @date 2024-07-28
 */
@Service
public class TobCameraServiceImpl implements ITobCameraService
{
    @Autowired
    private TobCameraMapper tobCameraMapper;

    /**
     * 查询摄像头
     *
     * @param cameraId 摄像头主键
     * @return 摄像头
     */
    @Override
    public TobCamera selectTobCameraByCameraId(Long cameraId)
    {
        return tobCameraMapper.selectTobCameraByCameraId(cameraId);
    }

    /**
     * 查询摄像头列表
     *
     * @param tobCamera 摄像头
     * @return 摄像头
     */
    @Override
    public List<TobCamera> selectTobCameraList(TobCamera tobCamera)
    {
        return tobCameraMapper.selectTobCameraList(tobCamera);
    }

    /**
     * 新增摄像头
     *
     * @param tobCamera 摄像头
     * @return 结果
     */
    @Override
    public int insertTobCamera(TobCamera tobCamera)
    {
        tobCamera.setCreateTime(DateUtils.getNowDate());
        return tobCameraMapper.insertTobCamera(tobCamera);
    }

    /**
     * 修改摄像头
     *
     * @param tobCamera 摄像头
     * @return 结果
     */
    @Override
    public int updateTobCamera(TobCamera tobCamera)
    {
        tobCamera.setUpdateTime(DateUtils.getNowDate());
        return tobCameraMapper.updateTobCamera(tobCamera);
    }

    /**
     * 批量删除摄像头
     *
     * @param cameraIds 需要删除的摄像头主键
     * @return 结果
     */
    @Override
    public int deleteTobCameraByCameraIds(Long[] cameraIds)
    {
        return tobCameraMapper.deleteTobCameraByCameraIds(cameraIds);
    }

    /**
     * 删除摄像头信息
     *
     * @param cameraId 摄像头主键
     * @return 结果
     */
    @Override
    public int deleteTobCameraByCameraId(Long cameraId)
    {
        return tobCameraMapper.deleteTobCameraByCameraId(cameraId);
    }

    /**
     * 根据地区数据查询摄像头列表
     * @param tobCamera
     * @return
     */
    @Override
    public List<TobCamera> selectTobCameraListByDistrictId(TobCamera tobCamera) {
        return tobCameraMapper.selectTobCameraListByDistrictId(tobCamera);
    }
    @Override
    public int simpleInsertTobCamera(TobCamera tobCamera)
    {
        System.out.println("========== simpleInsertTobCamera ==========");

        // 设置必填字段的默认值
        if (tobCamera.getStatus() == null) {
            tobCamera.setStatus("0");
        }
        if (tobCamera.getDelFlag() == null) {
            tobCamera.setDelFlag("0");
        }
        if (tobCamera.getResolutionRatio() == null) {
            tobCamera.setResolutionRatio("1");
        }
        if (tobCamera.getFrameRate() == null) {
            tobCamera.setFrameRate(25L);
        }
        if (tobCamera.getNightVision() == null) {
            tobCamera.setNightVision("0");
        }
        if (tobCamera.getCameraType() == null) {
            tobCamera.setCameraType("1");
        }
        if (tobCamera.getCameraApplicationType() == null) {
            tobCamera.setCameraApplicationType("1");
        }
        if (tobCamera.getLongitude() == null) {
            tobCamera.setLongitude(BigDecimal.ZERO);
        }
        if (tobCamera.getLatitude() == null) {
            tobCamera.setLatitude(BigDecimal.ZERO);
        }
        if (tobCamera.getConnectionType() == null) {
            tobCamera.setConnectionType("0");
        }
        if (tobCamera.getGuaranteePeriod() == null) {
            tobCamera.setGuaranteePeriod(new Date());
        }
        if (tobCamera.getInstallationDate() == null) {
            tobCamera.setInstallationDate(new Date());
        }

        tobCamera.setCreateTime(DateUtils.getNowDate());
        return tobCameraMapper.insertTobCamera(tobCamera);
    }
}
