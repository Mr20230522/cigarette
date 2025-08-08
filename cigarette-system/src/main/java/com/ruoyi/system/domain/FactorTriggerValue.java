package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class FactorTriggerValue extends BaseEntity {
    private Long id;

    @Excel(name = "因子键")
    private String factorKey;

    @Excel(name = "触发值")
    private String triggerValue;
}
