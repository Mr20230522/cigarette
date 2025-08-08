package com.ruoyi.system.service;


import com.ruoyi.system.domain.FactorConfig;
import java.util.List;

public interface IFactorConfigService {
    List<FactorConfig> selectFactorConfigList(FactorConfig filter);
    FactorConfig selectFactorConfigById(Long id);
    int insertFactorConfig(FactorConfig factorConfig);
    int updateFactorConfig(FactorConfig factorConfig);
    int deleteFactorConfigById(Long id);
}

