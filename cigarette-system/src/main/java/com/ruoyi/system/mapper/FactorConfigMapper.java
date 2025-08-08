package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FactorConfig;
import java.util.List;

public interface FactorConfigMapper {
    int insertFactorConfig(FactorConfig factorConfig);
    int updateFactorConfig(FactorConfig factorConfig);
    int deleteFactorConfigById(Long id);
    FactorConfig selectFactorConfigById(Long id);
    List<FactorConfig> selectFactorConfigList(FactorConfig factorConfig);
}

