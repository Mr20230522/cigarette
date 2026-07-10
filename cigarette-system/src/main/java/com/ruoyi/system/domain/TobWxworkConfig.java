package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 企业微信配置实体
 * 配置类型：1:文本 2:密码 3:数字
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobWxworkConfig extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 配置键 */
    private String configKey;

    /** 配置值 */
    private String configValue;

    /** 配置名称 */
    private String configName;

    /** 配置类型（1:文本 2:密码 3:数字） */
    private String configType;

    /** 是否启用（1:启用 0:禁用） */
    private Integer isActive;
}
