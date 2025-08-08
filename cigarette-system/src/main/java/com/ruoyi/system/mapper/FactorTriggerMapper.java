package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.FactorTrigger;
import java.util.List;

public interface FactorTriggerMapper {
    int insertFactorTrigger(FactorTrigger factorTrigger);
    int updateFactorTrigger(FactorTrigger factorTrigger);
    int deleteFactorTriggerById(Long id);
    FactorTrigger selectFactorTriggerById(Long id);
    List<FactorTrigger> selectFactorTriggerList(FactorTrigger factorTrigger);
}

