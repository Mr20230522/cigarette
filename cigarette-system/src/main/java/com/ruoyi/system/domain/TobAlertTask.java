package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 预警任务实体
 * 任务状态：0:待处理 1:已接受 2:处理中(已反馈) 3:已关闭
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobAlertTask extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务编号 */
    private String taskNo;

    /** 原始数据ID */
    private Long originalId;

    /** 关联接收表ID */
    private Long receiveId;

    /** 地域ID */
    private Integer locationId;

    /** 车牌号 */
    private String plate;

    /** 卡口名称 */
    private String cameraName;

    /** 抓拍时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date captureTime;

    /** 预警原因 */
    private String reason;

    /** 任务状态（0:待处理 1:已接受 2:处理中(已反馈) 3:已关闭） */
    private Integer status;

    /** 接受人ID */
    private String acceptUser;

    /** 当前推送人员索引 */
    private Integer assignIndex;

    /** 是否已推送管理员（1:已推送） */
    private Integer pushedToAdmin;

    /** 过期时间（创建后2小时自动关闭） */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date expireTime;
}
