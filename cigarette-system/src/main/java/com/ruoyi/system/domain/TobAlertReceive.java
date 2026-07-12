package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 预警数据接收实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobAlertReceive extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 接收编号 */
    private String receiveNo;

    /** 原始数据ID（上游系统推送的id） */
    private Long originalId;

    /** 预警原因 */
    private String reason;

    /** 关联trafficdata.Id */
    private Long trafficId;

    /** 车牌号 */
    private String plate;

    /** 卡口ID */
    private Integer cameraId;

    /** 卡口名称 */
    private String cameraName;

    /** 抓拍时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date captureTime;

    /** 处理状态（0:未处理 1:已处理） */
    private String processStatus;
}
