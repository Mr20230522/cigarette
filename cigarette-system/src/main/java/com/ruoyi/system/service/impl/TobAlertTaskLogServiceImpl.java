package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobAlertTaskLog;
import com.ruoyi.system.mapper.TobAlertTaskLogMapper;
import com.ruoyi.system.service.ITobAlertTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * 任务处理日志Service业务层处理
 */
@Service
public class TobAlertTaskLogServiceImpl implements ITobAlertTaskLogService {

    @Autowired
    private TobAlertTaskLogMapper tobAlertTaskLogMapper;

    @Override
    public List<TobAlertTaskLog> selectTobAlertTaskLogByTaskId(Long taskId) {
        return tobAlertTaskLogMapper.selectTobAlertTaskLogByTaskId(taskId);
    }

    @Override
    public List<TobAlertTaskLog> selectTobAlertTaskLogList(TobAlertTaskLog tobAlertTaskLog) {
        return tobAlertTaskLogMapper.selectTobAlertTaskLogList(tobAlertTaskLog);
    }

    @Override
    public int insertTobAlertTaskLog(TobAlertTaskLog tobAlertTaskLog) {
        return tobAlertTaskLogMapper.insertTobAlertTaskLog(tobAlertTaskLog);
    }
}
