package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobRegionPerson;
import com.ruoyi.system.mapper.TobRegionPersonMapper;
import com.ruoyi.system.service.ITobRegionPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 地域人员绑定Service业务层处理
 */
@Service
public class TobRegionPersonServiceImpl implements ITobRegionPersonService {

    @Autowired
    private TobRegionPersonMapper tobRegionPersonMapper;

    @Override
    public TobRegionPerson selectTobRegionPersonById(Long id) {
        return tobRegionPersonMapper.selectTobRegionPersonById(id);
    }

    @Override
    public List<TobRegionPerson> selectTobRegionPersonByLocationId(Integer locationId) {
        return tobRegionPersonMapper.selectTobRegionPersonByLocationId(locationId);
    }

    @Override
    public List<TobRegionPerson> selectTobRegionPersonList(TobRegionPerson tobRegionPerson) {
        return tobRegionPersonMapper.selectTobRegionPersonList(tobRegionPerson);
    }

    @Override
    public int insertTobRegionPerson(TobRegionPerson tobRegionPerson) {
        return tobRegionPersonMapper.insertTobRegionPerson(tobRegionPerson);
    }

    @Override
    public int updateTobRegionPerson(TobRegionPerson tobRegionPerson) {
        return tobRegionPersonMapper.updateTobRegionPerson(tobRegionPerson);
    }

    @Override
    public int deleteTobRegionPersonByIds(Long[] ids) {
        return tobRegionPersonMapper.deleteTobRegionPersonByIds(ids);
    }

    @Override
    public TobRegionPerson selectDistinctByUserId(String userId) {
        return tobRegionPersonMapper.selectDistinctByUserId(userId);
    }
}
