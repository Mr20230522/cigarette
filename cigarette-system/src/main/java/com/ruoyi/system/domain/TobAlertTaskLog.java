package com.ruoyi.system.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.core.domain.BaseEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/**
 * 任务处理日志实体
 * 操作类型：1:接受 2:拒绝 3:推送成功 4:推送失败 5:超时推管理员 6:超时自动关闭 7:提交反馈 8:手动关闭
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class TobAlertTaskLog extends BaseEntity {

    private static final long serialVersionUID = 1L;

    /** 主键ID */
    private Long id;

    /** 任务ID（关联tob_alert_task.id） */
    private Long taskId;

    /** 操作人ID */
    private String operateUser;

    /** 操作类型（1:接受 2:拒绝 3:推送成功 4:推送失败 5:超时推管理员 6:超时自动关闭 7:提交反馈 8:手动关闭） */
    private Integer operateType;

    /** 操作内容描述 */
    private String operateContent;

    /** 操作时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;
}
