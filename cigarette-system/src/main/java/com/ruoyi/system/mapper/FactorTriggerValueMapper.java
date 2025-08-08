package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.FactorTriggerValue;
import java.util.List;

public interface FactorTriggerValueMapper {
    List<FactorTriggerValue> selectFactorTriggerValueList(FactorTriggerValue filter);
    int insertFactorTriggerValue(FactorTriggerValue record);
    int deleteFactorTriggerValueById(Long id);
    int deleteFactorTriggerValueByIds(Long[] ids);
}
