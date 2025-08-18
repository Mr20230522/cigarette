package com.ruoyi.system.service.impl;

import com.ruoyi.common.utils.DateUtils;
import com.ruoyi.system.domain.ToVehicleFieldScore;
import com.ruoyi.system.mapper.ToVehicleFieldScoreMapper;
import com.ruoyi.system.service.IToVehicleFieldScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToVehicleFieldScoreServiceImpl implements IToVehicleFieldScoreService {

    @Autowired
    private ToVehicleFieldScoreMapper toVehicleFieldScoreMapper;

    @Override
    public ToVehicleFieldScore selectToVehicleFieldScoreById(Long id) {
        return toVehicleFieldScoreMapper.selectToVehicleFieldScoreById(id);
    }

    @Override
    public ToVehicleFieldScore selectByPlate(String plate) {
        return toVehicleFieldScoreMapper.selectByPlate(plate);
    }

    @Override
    public List<ToVehicleFieldScore> selectToVehicleFieldScoreList(ToVehicleFieldScore toVehicleFieldScore) {
        return toVehicleFieldScoreMapper.selectToVehicleFieldScoreList(toVehicleFieldScore);
    }

    @Override
    public int insertToVehicleFieldScore(ToVehicleFieldScore toVehicleFieldScore) {
        toVehicleFieldScore.setCreateTime(DateUtils.getNowDate());
        toVehicleFieldScore.setUpdateTime(DateUtils.getNowDate());
        return toVehicleFieldScoreMapper.insertToVehicleFieldScore(toVehicleFieldScore);
    }

    @Override
    public int updateToVehicleFieldScoreByPlate(ToVehicleFieldScore toVehicleFieldScore) {
        toVehicleFieldScore.setUpdateTime(DateUtils.getNowDate());
        return toVehicleFieldScoreMapper.updateToVehicleFieldScoreByPlate(toVehicleFieldScore);
    }

    @Override
    public int deleteToVehicleFieldScoreByIds(String ids) {
        return toVehicleFieldScoreMapper.deleteToVehicleFieldScoreById(Long.valueOf(ids));
    }
}