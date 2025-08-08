package com.ruoyi.system.service.impl;


import com.ruoyi.system.domain.FactorConfig;
import com.ruoyi.system.mapper.FactorConfigMapper;
import com.ruoyi.system.service.IFactorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FactorConfigServiceImpl implements IFactorConfigService {

    @Autowired
    private FactorConfigMapper factorConfigMapper;

    @Override
    public List<FactorConfig> selectFactorConfigList(FactorConfig filter) {
        return factorConfigMapper.selectFactorConfigList(filter);
    }

    @Override
    public FactorConfig selectFactorConfigById(Long id) {
        return factorConfigMapper.selectFactorConfigById(id);
    }

    @Override
    public int insertFactorConfig(FactorConfig factorConfig) {
        return factorConfigMapper.insertFactorConfig(factorConfig);
    }

    @Override
    public int updateFactorConfig(FactorConfig factorConfig) {
        return factorConfigMapper.updateFactorConfig(factorConfig);
    }

    @Override
    public int deleteFactorConfigById(Long id) {
        return factorConfigMapper.deleteFactorConfigById(id);
    }
}
