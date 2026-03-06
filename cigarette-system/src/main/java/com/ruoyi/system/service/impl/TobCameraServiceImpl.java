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
            System.out.println("   status为空，设置为默认值 '0'");
            tobCamera.setStatus("0");
        }
        if (tobCamera.getDelFlag() == null) {
            System.out.println("   delFlag为空，设置为默认值 '0'");
            tobCamera.setDelFlag("0");
        }
        if (tobCamera.getResolutionRatio() == null) {
            System.out.println("   resolutionRatio为空，设置为默认值 '1'");
            tobCamera.setResolutionRatio("1");
        }
        if (tobCamera.getFrameRate() == null) {
            System.out.println("   frameRate为空，设置为默认值 25");
            tobCamera.setFrameRate(25L);
        }
        if (tobCamera.getNightVision() == null) {
            System.out.println("   nightVision为空，设置为默认值 '0'");
            tobCamera.setNightVision("0");
        }
        if (tobCamera.getCameraType() == null) {
            System.out.println("   cameraType为空，设置为默认值 '1'");
            tobCamera.setCameraType("1");
        }
        if (tobCamera.getCameraApplicationType() == null) {
            System.out.println("   cameraApplicationType为空，设置为默认值 '1'");
            tobCamera.setCameraApplicationType("1");
        }
        if (tobCamera.getLongitude() == null) {
            System.out.println("   longitude为空，设置为默认值 0");
            tobCamera.setLongitude(BigDecimal.ZERO);
        }
        if (tobCamera.getLatitude() == null) {
            System.out.println("   latitude为空，设置为默认值 0");
            tobCamera.setLatitude(BigDecimal.ZERO);
        }
        if (tobCamera.getConnectionType() == null) {
            System.out.println("   connectionType为空，设置为默认值 '0'");
            tobCamera.setConnectionType("0");
        }
        if (tobCamera.getGuaranteePeriod() == null) {
            System.out.println("   guaranteePeriod为空，设置为当前时间");
            tobCamera.setGuaranteePeriod(new Date());
        }
        if (tobCamera.getInstallationDate() == null) {
            System.out.println("   installationDate为空，设置为当前时间");
            tobCamera.setInstallationDate(new Date());
        }

        tobCamera.setCreateTime(DateUtils.getNowDate());
        System.out.println("   createTime设置为: " + tobCamera.getCreateTime());

        System.out.println("开始调用 mapper 插入数据...");
        int result = tobCameraMapper.insertTobCamera(tobCamera);
        System.out.println("mapper 返回结果: " + result);

        return result;
    }
}
