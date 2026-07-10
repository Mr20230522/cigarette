package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobAlertTaskLog;
import java.util.List;

/**
 * 任务处理日志Service接口
 */
public interface ITobAlertTaskLogService {

    /**
     * 根据任务ID查询日志列表
     */
    List<TobAlertTaskLog> selectTobAlertTaskLogByTaskId(Long taskId);

    /**
     * 查询任务日志列表
     */
    List<TobAlertTaskLog> selectTobAlertTaskLogList(TobAlertTaskLog tobAlertTaskLog);

    /**
     * 新增任务日志
     */
    int insertTobAlertTaskLog(TobAlertTaskLog tobAlertTaskLog);
}
