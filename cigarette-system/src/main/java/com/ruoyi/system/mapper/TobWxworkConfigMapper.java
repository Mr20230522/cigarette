package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobWxworkConfig;
import java.util.List;

/**
 * 企业微信配置Mapper接口
 */
public interface TobWxworkConfigMapper {

    /**
     * 查询企微配置
     */
    TobWxworkConfig selectTobWxworkConfigById(Long id);

    /**
     * 根据配置键查询
     */
    TobWxworkConfig selectTobWxworkConfigByConfigKey(String configKey);

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
    int deleteTobWxworkConfigById(Long id);

    /**
     * 批量删除企微配置
     */
    int deleteTobWxworkConfigByIds(Long[] ids);
}
