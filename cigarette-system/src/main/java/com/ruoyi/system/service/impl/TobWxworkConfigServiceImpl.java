package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobWxworkConfig;
import com.ruoyi.system.mapper.TobWxworkConfigMapper;
import com.ruoyi.system.service.ITobWxworkConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 企业微信配置Service业务层处理
 */
@Service
public class TobWxworkConfigServiceImpl implements ITobWxworkConfigService {

    @Autowired
    private TobWxworkConfigMapper tobWxworkConfigMapper;

    @Override
    public TobWxworkConfig selectTobWxworkConfigById(Long id) {
        return tobWxworkConfigMapper.selectTobWxworkConfigById(id);
    }

    @Override
    public TobWxworkConfig selectTobWxworkConfigByConfigKey(String configKey) {
        return tobWxworkConfigMapper.selectTobWxworkConfigByConfigKey(configKey);
    }

    @Override
    public String getConfigValue(String configKey, String defaultValue) {
        TobWxworkConfig config = tobWxworkConfigMapper.selectTobWxworkConfigByConfigKey(configKey);
        if (config != null && config.getConfigValue() != null && !config.getConfigValue().isEmpty()) {
            return config.getConfigValue();
        }
        return defaultValue;
    }

    @Override
    public List<TobWxworkConfig> selectTobWxworkConfigList(TobWxworkConfig tobWxworkConfig) {
        return tobWxworkConfigMapper.selectTobWxworkConfigList(tobWxworkConfig);
    }

    @Override
    public int insertTobWxworkConfig(TobWxworkConfig tobWxworkConfig) {
        return tobWxworkConfigMapper.insertTobWxworkConfig(tobWxworkConfig);
    }

    @Override
    public int updateTobWxworkConfig(TobWxworkConfig tobWxworkConfig) {
        return tobWxworkConfigMapper.updateTobWxworkConfig(tobWxworkConfig);
    }

    @Override
    public int deleteTobWxworkConfigByIds(Long[] ids) {
        return tobWxworkConfigMapper.deleteTobWxworkConfigByIds(ids);
    }
}
