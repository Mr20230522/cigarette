package com.ruoyi.system.service.impl;


import com.ruoyi.system.domain.ToFactorTrigger;
import com.ruoyi.system.mapper.ToFactorTriggerMapper;
import com.ruoyi.system.service.IToFactorTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToFactorTriggerServiceImpl implements IToFactorTriggerService {

    @Autowired
    private ToFactorTriggerMapper factorTriggerMapper;

    @Override
    public List<ToFactorTrigger> selectFactorTriggerList(ToFactorTrigger filter) {
        return factorTriggerMapper.selectFactorTriggerList(filter);
    }

    @Override
    public ToFactorTrigger selectFactorTriggerById(Long id) {
        return factorTriggerMapper.selectFactorTriggerById(id);
    }

    @Override
    public int insertFactorTrigger(ToFactorTrigger factorTrigger) {
        return factorTriggerMapper.insertFactorTrigger(factorTrigger);
    }

    @Override
    public int updateFactorTrigger(ToFactorTrigger factorTrigger) {
        return factorTriggerMapper.updateFactorTrigger(factorTrigger);
    }

    @Override
    public int deleteFactorTriggerById(Long id) {
        return factorTriggerMapper.deleteFactorTriggerById(id);
    }
}
