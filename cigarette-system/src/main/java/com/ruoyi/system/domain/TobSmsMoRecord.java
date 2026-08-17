package com.ruoyi.system.domain;

import lombok.Data;

@Data
public class TobSmsMoRecord {
    private static final long serialVersionUID = 1L;

    private Long id;
    private String taskId;
    private String mobile;
    private String servicenumber;
    private String msgid;
    private String msg;
    private String uptime;
    private Integer status;   // 0-待处理，1-已处理，2-处理失败

}
