package com.ruoyi.system.mapper;


import com.ruoyi.system.domain.ToFactorTrigger;
import java.util.List;

public interface ToFactorTriggerMapper {
    int insertFactorTrigger(ToFactorTrigger factorTrigger);
    int updateFactorTrigger(ToFactorTrigger factorTrigger);
    int deleteFactorTriggerById(Long id);
    ToFactorTrigger selectFactorTriggerById(Long id);
    List<ToFactorTrigger> selectFactorTriggerList(ToFactorTrigger factorTrigger);
}

