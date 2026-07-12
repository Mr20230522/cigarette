package com.ruoyi.system.mapper;

import com.ruoyi.system.domain.TobAlertFeedback;
import java.util.List;

/**
 * 反馈结果Mapper接口
 */
public interface TobAlertFeedbackMapper {

    /**
     * 查询反馈结果
     */
    TobAlertFeedback selectTobAlertFeedbackById(Long id);

    /**
     * 根据任务ID查询反馈
     */
    TobAlertFeedback selectTobAlertFeedbackByTaskId(Long taskId);

    /**
     * 查询反馈结果列表
     */
    List<TobAlertFeedback> selectTobAlertFeedbackList(TobAlertFeedback tobAlertFeedback);

    /**
     * 新增反馈结果
     */
    int insertTobAlertFeedback(TobAlertFeedback tobAlertFeedback);
}
