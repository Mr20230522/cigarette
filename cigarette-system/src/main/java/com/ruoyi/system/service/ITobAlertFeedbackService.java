package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobAlertFeedback;
import java.util.List;

/**
 * 反馈结果Service接口
 */
public interface ITobAlertFeedbackService {

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
