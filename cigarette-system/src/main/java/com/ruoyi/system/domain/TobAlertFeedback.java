package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 反馈结果实体
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobAlertFeedback extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务ID（关联tob_alert_task.id） */
    private Long taskId;

    /** 处理人ID */
    private String userId;

    /** 是否符合预警（0:不符合 1:符合） */
    private Integer isMeetAlert;

    /** 是否违法（0:未违法 1:违法） */
    private Integer isIllegal;

    /** 追加说明 */
    private String remark;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
