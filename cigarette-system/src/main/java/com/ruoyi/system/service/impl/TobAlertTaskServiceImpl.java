package com.ruoyi.system.service.impl;

import cn.hutool.core.date.DateUtil;
import com.ruoyi.system.domain.*;
import com.ruoyi.system.mapper.TobAlertTaskMapper;
import com.ruoyi.system.mapper.TobCameraRegionMapper;
import com.ruoyi.system.service.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * 预警任务Service业务层处理
 * 任务状态：0:待处理 1:已接受 2:处理中(已反馈) 3:已关闭
 */
@Service
public class TobAlertTaskServiceImpl implements ITobAlertTaskService {

    private static final Logger log = LoggerFactory.getLogger(TobAlertTaskServiceImpl.class);

    @Autowired
    private TobAlertTaskMapper tobAlertTaskMapper;

    @Autowired
    private ITobAlertTaskLogService tobAlertTaskLogService;

    @Autowired
    private ITobAlertFeedbackService tobAlertFeedbackService;

    @Autowired
    private ITobRegionPersonService tobRegionPersonService;

    @Autowired
    private WxworkPushServiceImpl wxworkPushService;

    @Autowired
    private TobCameraRegionMapper tobCameraRegionMapper;

    @Override
    public TobAlertTask selectTobAlertTaskById(Long id) {
        return tobAlertTaskMapper.selectTobAlertTaskById(id);
    }

    @Override
    public List<TobAlertTask> selectTobAlertTaskList(TobAlertTask tobAlertTask) {
        return tobAlertTaskMapper.selectTobAlertTaskList(tobAlertTask);
    }

    @Override
    public int insertTobAlertTask(TobAlertTask tobAlertTask) {
        return tobAlertTaskMapper.insertTobAlertTask(tobAlertTask);
    }

    @Override
    public int updateTobAlertTask(TobAlertTask tobAlertTask) {
        return tobAlertTaskMapper.updateTobAlertTask(tobAlertTask);
    }

    @Override
    public int deleteTobAlertTaskByIds(Long[] ids) {
        return tobAlertTaskMapper.deleteTobAlertTaskByIds(ids);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int acceptTask(Long taskId, String userId) {
        // 原子更新状态：0(待处理) → 1(已接受)
        int rows = tobAlertTaskMapper.updateTobAlertTaskStatus(taskId, 0, 1, userId);
        if (rows == 0) {
            log.warn("接受任务失败，任务已处理或不存在，taskId={}", taskId);
            return 0;
        }

        // 记录日志
        TobAlertTaskLog taskLog = new TobAlertTaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setOperateUser(userId);
        taskLog.setOperateType(1);
        taskLog.setOperateContent("接受任务");
        tobAlertTaskLogService.insertTobAlertTaskLog(taskLog);

        log.info("任务接受成功，taskId={}, userId={}", taskId, userId);
        notifyGroupStatus(taskId, userId, "接受任务", "处理中");
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int rejectTask(Long taskId, String userId) {
        TobAlertTask task = tobAlertTaskMapper.selectTobAlertTaskById(taskId);
        if (task == null || task.getStatus() != 0) {
            return 0;
        }

        // 记录拒绝日志
        TobAlertTaskLog taskLog = new TobAlertTaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setOperateUser(userId);
        taskLog.setOperateType(2);
        taskLog.setOperateContent("拒绝任务");
        tobAlertTaskLogService.insertTobAlertTaskLog(taskLog);

        // 清空接受人，索引+1
        task.setAcceptUser(null);
        task.setAssignIndex(task.getAssignIndex() != null ? task.getAssignIndex() + 1 : 1);
        tobAlertTaskMapper.updateTobAlertTask(task);

        // 推送给下一个人
        List<TobRegionPerson> personList = tobRegionPersonService.selectTobRegionPersonByLocationId(task.getLocationId());
        if (personList != null && task.getAssignIndex() < personList.size()) {
            TobRegionPerson nextPerson = personList.get(task.getAssignIndex());
            boolean success = wxworkPushService.pushTextCard(task, nextPerson.getUserId());
            TobAlertTaskLog pushLog = new TobAlertTaskLog();
            pushLog.setTaskId(taskId);
            pushLog.setOperateUser("system");
            pushLog.setOperateType(success ? 3 : 4);
            pushLog.setOperateContent(success ?
                    "重新推送成功，推送至用户[" + nextPerson.getUserId() + "]" :
                    "重新推送失败，推送至用户[" + nextPerson.getUserId() + "]");
            tobAlertTaskLogService.insertTobAlertTaskLog(pushLog);
        } else {
            // 所有人员均拒绝，推管理员
            List<TobRegionPerson> adminList = tobRegionPersonService.selectTobRegionPersonByLocationId(0);
            task.setPushedToAdmin(1);
            tobAlertTaskMapper.updateTobAlertTask(task);
            if (adminList != null && !adminList.isEmpty()) {
                wxworkPushService.pushTextCard(task, adminList.get(0).getUserId());
            }
            TobAlertTaskLog pushLog = new TobAlertTaskLog();
            pushLog.setTaskId(taskId);
            pushLog.setOperateUser("system");
            pushLog.setOperateType(5);
            pushLog.setOperateContent("所有人员均已拒绝，推送至管理员");
            tobAlertTaskLogService.insertTobAlertTaskLog(pushLog);
        }

        log.info("任务拒绝成功，taskId={}, userId={}", taskId, userId);
        notifyGroupStatus(taskId, userId, "拒绝任务", "已重新分配");
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int closeTask(Long taskId, String operateUser) {
        TobAlertTask task = tobAlertTaskMapper.selectTobAlertTaskById(taskId);
        if (task == null) {
            return 0;
        }

        task.setStatus(3);
        tobAlertTaskMapper.updateTobAlertTask(task);

        // 记录日志
        TobAlertTaskLog taskLog = new TobAlertTaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setOperateUser(operateUser);
        taskLog.setOperateType(8);
        taskLog.setOperateContent("手动关闭任务");
        tobAlertTaskLogService.insertTobAlertTaskLog(taskLog);

        log.info("手动关闭任务，taskId={}, operateUser={}", taskId, operateUser);
        notifyGroupStatus(taskId, operateUser, "手动关闭", "已关闭");
        return 1;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int submitFeedback(Long taskId, String userId, Integer isMeetAlert, Integer isIllegal, String remark) {
        TobAlertTask task = tobAlertTaskMapper.selectTobAlertTaskById(taskId);
        if (task == null || task.getStatus() != 1) {
            log.warn("提交反馈失败，任务状态不正确，taskId={}, status={}", taskId, task != null ? task.getStatus() : null);
            return 0;
        }

        // 保存反馈
        TobAlertFeedback feedback = new TobAlertFeedback();
        feedback.setTaskId(taskId);
        feedback.setUserId(userId);
        feedback.setIsMeetAlert(isMeetAlert);
        feedback.setIsIllegal(isIllegal);
        feedback.setRemark(remark);
        tobAlertFeedbackService.insertTobAlertFeedback(feedback);

        // 更新任务状态：1(已接受) → 2(处理中/已反馈)
        task.setStatus(2);
        tobAlertTaskMapper.updateTobAlertTask(task);

        // 记录反馈日志
        TobAlertTaskLog taskLog = new TobAlertTaskLog();
        taskLog.setTaskId(taskId);
        taskLog.setOperateUser(userId);
        taskLog.setOperateType(7);
        String content = "提交反馈：";
        content += isMeetAlert == 1 ? "符合预警" : "不符合预警";
        content += " / ";
        content += isIllegal == 1 ? "违法" : "未违法";
        taskLog.setOperateContent(content);
        tobAlertTaskLogService.insertTobAlertTaskLog(taskLog);

        log.info("反馈提交成功，taskId={}, userId={}", taskId, userId);
        notifyGroupStatus(taskId, userId, "提交反馈：" + (isMeetAlert == 1 ? "符合预警" : "不符合预警") + " / " + (isIllegal == 1 ? "违法" : "未违法"), "已反馈");
        return 1;
    }

    @Override
    public TobAlertTask selectTobAlertTaskByOriginalId(Long originalId) {
        return tobAlertTaskMapper.selectTobAlertTaskByOriginalId(originalId);
    }

    @Override
    public List<TobAlertTask> selectTimeoutTasks() {
        return tobAlertTaskMapper.selectTimeoutTasks();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void processTimeoutTasks() {
        List<TobAlertTask> timeoutTasks = tobAlertTaskMapper.selectTimeoutTasks();
        if (timeoutTasks == null || timeoutTasks.isEmpty()) {
            return;
        }

        for (TobAlertTask task : timeoutTasks) {
            long minutesAgo = (System.currentTimeMillis() - task.getCreateTime().getTime()) / 60000;

            if (minutesAgo >= 120 && task.getPushedToAdmin() == 0) {
                // 超过2小时，硬超时关闭
                task.setStatus(3);
                tobAlertTaskMapper.updateTobAlertTask(task);

                TobAlertTaskLog taskLog = new TobAlertTaskLog();
                taskLog.setTaskId(task.getId());
                taskLog.setOperateUser("system");
                taskLog.setOperateType(6);
                taskLog.setOperateContent("超时自动关闭（创建超过2小时）");
                tobAlertTaskLogService.insertTobAlertTaskLog(taskLog);

                log.info("任务硬超时关闭，taskId={}", task.getId());
                notifyGroupStatus(task.getId(), "system", "超时自动关闭", "已关闭");
            } else if (minutesAgo >= 2 && task.getPushedToAdmin() == 0) {
                // 超过2分钟未处理，推管理员
                task.setPushedToAdmin(1);
                tobAlertTaskMapper.updateTobAlertTask(task);

                List<TobRegionPerson> adminList = tobRegionPersonService.selectTobRegionPersonByLocationId(0);
                if (adminList != null && !adminList.isEmpty()) {
                    wxworkPushService.pushTextCard(task, adminList.get(0).getUserId());
                }

                TobAlertTaskLog taskLog = new TobAlertTaskLog();
                taskLog.setTaskId(task.getId());
                taskLog.setOperateUser("system");
                taskLog.setOperateType(5);
                taskLog.setOperateContent("超时升级推送至管理员（创建超过2分钟未处理）");
                tobAlertTaskLogService.insertTobAlertTaskLog(taskLog);

                log.info("任务超时升级推管理员，taskId={}", task.getId());
                notifyGroupStatus(task.getId(), "system", "超时升级推送管理员", "待处理");
            }
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public int pushTask(Long taskId) {
        TobAlertTask task = tobAlertTaskMapper.selectTobAlertTaskById(taskId);
        if (task == null) {
            log.warn("手动推送失败，任务不存在，taskId={}", taskId);
            return 0;
        }
        if (task.getStatus() == 3) {
            log.warn("手动推送失败，任务已关闭，taskId={}", taskId);
            return 0;
        }

        // 重置推送索引从头开始推
        task.setAssignIndex(0);
        task.setPushedToAdmin(0);
        tobAlertTaskMapper.updateTobAlertTask(task);

        // 查询绑定人员
        List<TobRegionPerson> personList = tobRegionPersonService.selectTobRegionPersonByLocationId(task.getLocationId());
        if (personList == null || personList.isEmpty()) {
            // 无绑定人员，尝试推管理员
            task.setPushedToAdmin(1);
            tobAlertTaskMapper.updateTobAlertTask(task);
            List<TobRegionPerson> adminList = tobRegionPersonService.selectTobRegionPersonByLocationId(0);
            if (adminList != null && !adminList.isEmpty()) {
                boolean success = wxworkPushService.pushTextCard(task, adminList.get(0).getUserId());
                TobAlertTaskLog pushLog = new TobAlertTaskLog();
                pushLog.setTaskId(taskId);
                pushLog.setOperateUser("system");
                pushLog.setOperateType(success ? 3 : 4);
                pushLog.setOperateContent(success ?
                        "手动推送至管理员[" + adminList.get(0).getUserId() + "]" :
                        "手动推送管理员失败");
                tobAlertTaskLogService.insertTobAlertTaskLog(pushLog);
            }
            return 1;
        }

        // 推送给首个绑定人员
        TobRegionPerson firstPerson = personList.get(0);
        boolean success = wxworkPushService.pushTextCard(task, firstPerson.getUserId());

        TobAlertTaskLog pushLog = new TobAlertTaskLog();
        pushLog.setTaskId(taskId);
        pushLog.setOperateUser("system");
        if (success) {
            pushLog.setOperateType(3);
            pushLog.setOperateContent("手动推送成功，推送至用户[" + firstPerson.getUserId() + "]");
        } else {
            pushLog.setOperateType(4);
            pushLog.setOperateContent("手动推送失败，推送至用户[" + firstPerson.getUserId() + "]");
        }
        tobAlertTaskLogService.insertTobAlertTaskLog(pushLog);

        log.info("手动推送完成，taskId={}, 结果={}", taskId, success ? "成功" : "失败");
        if (success) {
            notifyGroupStatus(taskId, "system", "手动推送", "重新分配");
        }
        return 1;   // 任务有效，始终返回成功；推送结果由日志记录
    }

    /**
     * 群机器人通知（旁路，失败不影响主流程）
     */
    private void notifyGroupStatus(Long taskId, String operator, String action, String statusText) {
        try {
            TobAlertTask task = tobAlertTaskMapper.selectTobAlertTaskById(taskId);
            if (task == null) return;

            // 查该地域是否配置了群Webhook
            TobCameraRegion region = tobCameraRegionMapper.selectTobCameraRegionByLocationId(task.getLocationId());
            if (region == null || region.getGroupWebhookUrl() == null || region.getGroupWebhookUrl().isEmpty()) {
                return;
            }

            String line1 = String.format("**[%s]** 车牌：<font color=\"warning\">%s</font>",
                    action, task.getPlate() != null ? task.getPlate() : "-");
            String line2 = String.format("> 原因：%s", task.getReason());
            String line3 = String.format("> 卡口：%s", task.getCameraName() != null ? task.getCameraName() : "-");
            String line4 = String.format("> 操作人：%s  |  状态：%s", operator, statusText);

            String content = line1 + "\n" + line2 + "\n" + line3 + "\n" + line4;
            wxworkPushService.notifyGroup(region.getGroupWebhookUrl(), content);
        } catch (Exception e) {
            log.warn("群通知发送异常, taskId={}: {}", taskId, e.getMessage());
        }
    }
}
