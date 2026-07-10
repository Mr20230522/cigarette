package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 卡口地域映射实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobCameraRegion extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 卡口ID（对应trafficdata.CameraId） */
    private Integer cameraId;

    /** 地域ID */
    private Integer locationId;

    /** 地域名称 */
    private String locationName;

    /** 是否生效（1:生效 0:失效） */
    private Integer isActive;

    /** 企微群机器人Webhook地址（用于同步通知群聊） */
    private String groupWebhookUrl;
}
