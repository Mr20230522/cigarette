package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobAlertTaskLog;
import java.util.List;

/**
 * 任务处理日志Mapper接口
 */
public interface TobAlertTaskLogMapper {

    /**
     * 查询任务日志
     */
    TobAlertTaskLog selectTobAlertTaskLogById(Long id);

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
