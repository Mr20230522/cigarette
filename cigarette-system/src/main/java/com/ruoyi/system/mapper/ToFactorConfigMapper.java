package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.ToFactorConfig;
import java.util.List;

public interface ToFactorConfigMapper {
    int insertFactorConfig(ToFactorConfig factorConfig);
    int updateFactorConfig(ToFactorConfig factorConfig);
    int deleteFactorConfigById(Long id);
    ToFactorConfig selectFactorConfigById(Long id);
    List<ToFactorConfig> selectFactorConfigList(ToFactorConfig factorConfig);
}

