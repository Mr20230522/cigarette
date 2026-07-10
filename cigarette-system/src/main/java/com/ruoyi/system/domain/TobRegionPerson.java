package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 地域人员绑定实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobRegionPerson extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 地域ID */
    private Integer locationId;

    /** 企业微信用户ID */
    private String userId;

    /** 用户姓名 */
    private String userName;

    /** 推送优先级（值越小越优先推送） */
    private Integer sortOrder;

    /** 是否生效（1:生效 0:失效） */
    private Integer isActive;
}
