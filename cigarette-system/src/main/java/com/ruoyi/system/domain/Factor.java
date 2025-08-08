package com.ruoyi.system.domain;


import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

@Data
public class Factor extends BaseEntity {
    private Long id;

    @Excel(name = "因子键")
    private String factorKey;

    @Excel(name = "因子值")
    private Integer factorValue;

    @Excel(name = "描述")
    private String description;
}
