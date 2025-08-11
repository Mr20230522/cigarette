package com.ruoyi.system.service;


import com.ruoyi.system.domain.ToFactorConfig;
import java.util.List;

public interface IToFactorConfigService {
    List<ToFactorConfig> selectFactorConfigList(ToFactorConfig filter);
    ToFactorConfig selectFactorConfigById(Long id);
    int insertFactorConfig(ToFactorConfig factorConfig);
    int updateFactorConfig(ToFactorConfig factorConfig);
    int deleteFactorConfigById(Long id);
}

