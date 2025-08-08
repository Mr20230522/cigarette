package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;

import java.util.Date;

@Data
public class FactorTrigger extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 因子ID */
    private Long factorId;

    /** 触发值 */
    private String triggerValue;

    /** 更新时间 */
    private Date updatedAt;
}
