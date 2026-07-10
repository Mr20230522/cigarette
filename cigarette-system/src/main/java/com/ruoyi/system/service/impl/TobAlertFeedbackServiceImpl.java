package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobAlertFeedback;
import com.ruoyi.system.mapper.TobAlertFeedbackMapper;
import com.ruoyi.system.service.ITobAlertFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 反馈结果Service业务层处理
 */
@Service
public class TobAlertFeedbackServiceImpl implements ITobAlertFeedbackService {

    @Autowired
    private TobAlertFeedbackMapper tobAlertFeedbackMapper;

    @Override
    public TobAlertFeedback selectTobAlertFeedbackByTaskId(Long taskId) {
        return tobAlertFeedbackMapper.selectTobAlertFeedbackByTaskId(taskId);
    }

    @Override
    public List<TobAlertFeedback> selectTobAlertFeedbackList(TobAlertFeedback tobAlertFeedback) {
        return tobAlertFeedbackMapper.selectTobAlertFeedbackList(tobAlertFeedback);
    }

    @Override
    public int insertTobAlertFeedback(TobAlertFeedback tobAlertFeedback) {
        return tobAlertFeedbackMapper.insertTobAlertFeedback(tobAlertFeedback);
    }
}
