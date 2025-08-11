package com.ruoyi.system.service.impl;


import com.ruoyi.system.domain.ToFactorConfig;
import com.ruoyi.system.mapper.ToFactorConfigMapper;
import com.ruoyi.system.service.IToFactorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ToFactorConfigServiceImpl implements IToFactorConfigService {

    @Autowired
    private ToFactorConfigMapper factorConfigMapper;

    @Override
    public List<ToFactorConfig> selectFactorConfigList(ToFactorConfig filter) {
        return factorConfigMapper.selectFactorConfigList(filter);
    }

    @Override
    public ToFactorConfig selectFactorConfigById(Long id) {
        return factorConfigMapper.selectFactorConfigById(id);
    }

    @Override
    public int insertFactorConfig(ToFactorConfig factorConfig) {
        return factorConfigMapper.insertFactorConfig(factorConfig);
    }

    @Override
    public int updateFactorConfig(ToFactorConfig factorConfig) {
        return factorConfigMapper.updateFactorConfig(factorConfig);
    }

    @Override
    public int deleteFactorConfigById(Long id) {
        return factorConfigMapper.deleteFactorConfigById(id);
    }
}
