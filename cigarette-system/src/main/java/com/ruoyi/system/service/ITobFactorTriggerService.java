package com.ruoyi.system.service;


import com.ruoyi.system.domain.ToFactorTrigger;
import java.util.List;

public interface ITobFactorTriggerService {
    List<ToFactorTrigger> selectFactorTriggerList(ToFactorTrigger filter);
    ToFactorTrigger selectFactorTriggerById(Long id);
    int insertFactorTrigger(ToFactorTrigger factorTrigger);
    int updateFactorTrigger(ToFactorTrigger factorTrigger);
    int deleteFactorTriggerById(Long id);
}
