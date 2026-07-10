package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobCameraRegion;
import com.ruoyi.system.mapper.TobCameraRegionMapper;
import com.ruoyi.system.service.ITobCameraRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 卡口地域映射Service业务层处理
 */
@Service
public class TobCameraRegionServiceImpl implements ITobCameraRegionService {

    @Autowired
    private TobCameraRegionMapper tobCameraRegionMapper;

    @Override
    public TobCameraRegion selectTobCameraRegionById(Long id) {
        return tobCameraRegionMapper.selectTobCameraRegionById(id);
    }

    @Override
    public TobCameraRegion selectTobCameraRegionByCameraId(Integer cameraId) {
        return tobCameraRegionMapper.selectTobCameraRegionByCameraId(cameraId);
    }

    @Override
    public List<TobCameraRegion> selectTobCameraRegionList(TobCameraRegion tobCameraRegion) {
        return tobCameraRegionMapper.selectTobCameraRegionList(tobCameraRegion);
    }

    @Override
    public int insertTobCameraRegion(TobCameraRegion tobCameraRegion) {
        return tobCameraRegionMapper.insertTobCameraRegion(tobCameraRegion);
    }

    @Override
    public int updateTobCameraRegion(TobCameraRegion tobCameraRegion) {
        return tobCameraRegionMapper.updateTobCameraRegion(tobCameraRegion);
    }

    @Override
    public int deleteTobCameraRegionByIds(Long[] ids) {
        return tobCameraRegionMapper.deleteTobCameraRegionByIds(ids);
    }
}
