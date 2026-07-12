package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobCaseHistory;
import com.ruoyi.system.mapper.TobCaseHistoryMapper;
import com.ruoyi.system.service.ITobCaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 历史案件车辆信息 服务实现层
 */
@Service
public class TobCaseHistoryServiceImpl implements ITobCaseHistoryService {

    @Autowired
    private TobCaseHistoryMapper caseHistoryMapper;

    @Override
    public List<TobCaseHistory> listAll() {
        return caseHistoryMapper.selectAll();
    }

    @Override
    public TobCaseHistory getByPlate(String plate) {
        return caseHistoryMapper.selectByPlate(plate);
    }

    @Override
    public TobCaseHistory getById(Long id) {
        return caseHistoryMapper.selectById(id);
    }

    @Override
    public int insert(TobCaseHistory record) {
        return caseHistoryMapper.insert(record);
    }

    @Override
    public int update(TobCaseHistory record) {
        return caseHistoryMapper.updateById(record);
    }

    @Override
    public List<TobCaseHistory> searchByPlate(String plate) {
        return caseHistoryMapper.searchByPlate(plate);
    }
}
