package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobWxworkConfig;
import java.util.List;

/**
 * 企业微信配置Service接口
 */
public interface ITobWxworkConfigService {

    /**
     * 查询企微配置
     */
    TobWxworkConfig selectTobWxworkConfigById(Long id);

    /**
     * 根据配置键查询
     */
    TobWxworkConfig selectTobWxworkConfigByConfigKey(String configKey);

    /**
     * 获取配置值（DB优先，无则用application.yml默认值）
     */
    String getConfigValue(String configKey, String defaultValue);

    /**
     * 查询企微配置列表
     */
    List<TobWxworkConfig> selectTobWxworkConfigList(TobWxworkConfig tobWxworkConfig);

    /**
     * 新增企微配置
     */
    int insertTobWxworkConfig(TobWxworkConfig tobWxworkConfig);

    /**
     * 修改企微配置
     */
    int updateTobWxworkConfig(TobWxworkConfig tobWxworkConfig);

    /**
     * 删除企微配置
     */
    int deleteTobWxworkConfigByIds(Long[] ids);
}
