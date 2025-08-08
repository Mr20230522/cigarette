package com.ruoyi.system.service;

import com.ruoyi.system.domain.FactorTriggerValue;
import java.util.List;

public interface IFactorTriggerService {

    /**
     * 查询触发值列表
     */
    List<FactorTriggerValue> selectFactorTriggerValueList(FactorTriggerValue filter);

    /**
     * 新增触发值
     */
    int insertFactorTriggerValue(FactorTriggerValue record);

    /**
     * 删除触发值
     */
    int deleteFactorTriggerValueByIds(Long[] ids);

    /**
     * 删除单个触发值
     */
    int deleteFactorTriggerValueById(Long id);
}
