package com.ruoyi.system.service;


import com.ruoyi.system.domain.FactorTrigger;
import java.util.List;

public interface IFactorTriggerService {
    List<FactorTrigger> selectFactorTriggerList(FactorTrigger filter);
    FactorTrigger selectFactorTriggerById(Long id);
    int insertFactorTrigger(FactorTrigger factorTrigger);
    int updateFactorTrigger(FactorTrigger factorTrigger);
    int deleteFactorTriggerById(Long id);
}
