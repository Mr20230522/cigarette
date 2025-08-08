package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.FactorTriggerValue;
import com.ruoyi.system.mapper.FactorTriggerValueMapper;
import com.ruoyi.system.service.IFactorTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class FactorTriggerServiceImpl implements IFactorTriggerService {

    @Autowired
    private FactorTriggerValueMapper factorTriggerValueMapper;

    @Override
    public List<FactorTriggerValue> selectFactorTriggerValueList(FactorTriggerValue filter) {
        return factorTriggerValueMapper.selectFactorTriggerValueList(filter);
    }

    @Override
    public int insertFactorTriggerValue(FactorTriggerValue record) {
        return factorTriggerValueMapper.insertFactorTriggerValue(record);
    }

    @Override
    public int deleteFactorTriggerValueByIds(Long[] ids) {
        return factorTriggerValueMapper.deleteFactorTriggerValueByIds(ids);
    }

    @Override
    public int deleteFactorTriggerValueById(Long id) {
        return factorTriggerValueMapper.deleteFactorTriggerValueById(id);
    }
}
