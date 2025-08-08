package com.ruoyi.system.service.impl;


import com.ruoyi.system.domain.FactorTrigger;
import com.ruoyi.system.mapper.FactorTriggerMapper;
import com.ruoyi.system.service.IFactorTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactorTriggerServiceImpl implements IFactorTriggerService {

    @Autowired
    private FactorTriggerMapper factorTriggerMapper;

    @Override
    public List<FactorTrigger> selectFactorTriggerList(FactorTrigger filter) {
        return factorTriggerMapper.selectFactorTriggerList(filter);
    }

    @Override
    public FactorTrigger selectFactorTriggerById(Long id) {
        return factorTriggerMapper.selectFactorTriggerById(id);
    }

    @Override
    public int insertFactorTrigger(FactorTrigger factorTrigger) {
        return factorTriggerMapper.insertFactorTrigger(factorTrigger);
    }

    @Override
    public int updateFactorTrigger(FactorTrigger factorTrigger) {
        return factorTriggerMapper.updateFactorTrigger(factorTrigger);
    }

    @Override
    public int deleteFactorTriggerById(Long id) {
        return factorTriggerMapper.deleteFactorTriggerById(id);
    }
}
