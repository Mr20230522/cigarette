package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobAlertTask;
import java.util.List;

/**
 * 预警任务Service接口
 */
public interface ITobAlertTaskService {

    /**
     * 查询预警任务
     */
    TobAlertTask selectTobAlertTaskById(Long id);

    /**
     * 查询预警任务列表
     */
    List<TobAlertTask> selectTobAlertTaskList(TobAlertTask tobAlertTask);

    /**
     * 新增预警任务（手动创建）
     */
    int insertTobAlertTask(TobAlertTask tobAlertTask);

    /**
     * 修改预警任务
     */
    int updateTobAlertTask(TobAlertTask tobAlertTask);

    /**
     * 删除预警任务
     */
    int deleteTobAlertTaskByIds(Long[] ids);

    /**
     * 接受任务
     */
    int acceptTask(Long taskId, String userId);

    /**
     * 拒绝任务（自动推送给下一个人）
     */
    int rejectTask(Long taskId, String userId);

    /**
     * 关闭任务
     */
    int closeTask(Long taskId, String operateUser);

    /**
     * 提交反馈
     */
    int submitFeedback(Long taskId, String userId, Integer isMeetAlert, Integer isIllegal, String remark);

    /**
     * 根据原始数据ID查询
     */
    TobAlertTask selectTobAlertTaskByOriginalId(Long originalId);

    /**
     * 查询超时未处理任务
     */
    List<TobAlertTask> selectTimeoutTasks();

    /**
     * 处理超时任务（升级推管理员 / 硬超时关闭）
     */
    void processTimeoutTasks();

    /**
     * 手动推送任务（重置推送索引，重新推送给第一个绑定人员）
     */
    int pushTask(Long taskId);

    /**
     * 判断某人是否忙碌（有status=1的处理中任务）
     */
    boolean isPersonBusy(String userId);
}
