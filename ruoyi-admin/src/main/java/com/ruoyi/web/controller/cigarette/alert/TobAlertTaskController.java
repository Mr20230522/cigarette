package com.ruoyi.web.controller.cigarette.alert;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.IdUtil;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobAlertTask;
import com.ruoyi.system.domain.TobAlertFeedback;
import com.ruoyi.system.service.ITobAlertTaskService;
import com.ruoyi.system.service.ITobAlertFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.Map;

/**
 * 预警任务管理Controller
 */
@RestController
@RequestMapping("/cigarette/alert/task")
public class TobAlertTaskController extends BaseController {

    @Autowired
    private ITobAlertTaskService tobAlertTaskService;

    @Autowired
    private ITobAlertFeedbackService tobAlertFeedbackService;

    /**
     * 查询任务列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:list')")
    @GetMapping("/list")
    @DataScope(deptAlias = "", userAlias = "")
    public TableDataInfo list(TobAlertTask tobAlertTask) {
        startPage();
        return getDataTable(tobAlertTaskService.selectTobAlertTaskList(tobAlertTask));
    }

    /**
     * 获取任务详情
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        AjaxResult ajax = AjaxResult.success(tobAlertTaskService.selectTobAlertTaskById(id));
        // 同时返回关联的反馈
        TobAlertFeedback feedback = tobAlertFeedbackService.selectTobAlertFeedbackByTaskId(id);
        ajax.put("feedback", feedback);
        return ajax;
    }

    /**
     * 手动新增任务
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:add')")
    @Log(title = "预警任务", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobAlertTask tobAlertTask) {
        tobAlertTask.setTaskNo("T" + DateUtil.format(new Date(), "yyyyMMdd")
                + IdUtil.fastSimpleUUID().substring(0, 6).toUpperCase());
        // 手动创建的任务：originalId设为负时间戳避免UNIQUE冲突，receiveId=0表示无关联接收记录
        tobAlertTask.setOriginalId(tobAlertTask.getOriginalId() != null ? tobAlertTask.getOriginalId() : -System.currentTimeMillis());
        tobAlertTask.setReceiveId(tobAlertTask.getReceiveId() != null ? tobAlertTask.getReceiveId() : 0L);
        tobAlertTask.setLocationId(tobAlertTask.getLocationId() != null ? tobAlertTask.getLocationId() : 0);
        tobAlertTask.setReason(tobAlertTask.getReason() != null ? tobAlertTask.getReason() : "");
        tobAlertTask.setStatus(tobAlertTask.getStatus() != null ? tobAlertTask.getStatus() : 0);
        tobAlertTask.setAssignIndex(tobAlertTask.getAssignIndex() != null ? tobAlertTask.getAssignIndex() : 0);
        tobAlertTask.setPushedToAdmin(tobAlertTask.getPushedToAdmin() != null ? tobAlertTask.getPushedToAdmin() : 0);
        tobAlertTask.setCreateBy(getUsername());
        int rows = tobAlertTaskService.insertTobAlertTask(tobAlertTask);
        return toAjax(rows);
    }

    /**
     * 修改任务
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:edit')")
    @Log(title = "预警任务", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobAlertTask tobAlertTask) {
        tobAlertTask.setUpdateBy(getUsername());
        int rows = tobAlertTaskService.updateTobAlertTask(tobAlertTask);
        return toAjax(rows);
    }

    /**
     * 删除任务
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:remove')")
    @Log(title = "预警任务", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tobAlertTaskService.deleteTobAlertTaskByIds(ids));
    }

    /**
     * 接受任务
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:accept')")
    @PutMapping("/accept/{id}")
    public AjaxResult accept(@PathVariable("id") Long id) {
        int rows = tobAlertTaskService.acceptTask(id, getUsername());
        return rows > 0 ? success() : error("任务已被处理或不存在");
    }

    /**
     * 拒绝任务
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:reject')")
    @PutMapping("/reject/{id}")
    public AjaxResult reject(@PathVariable("id") Long id) {
        int rows = tobAlertTaskService.rejectTask(id, getUsername());
        return rows > 0 ? success() : error("操作失败");
    }

    /**
     * 关闭任务
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:close')")
    @PutMapping("/close/{id}")
    public AjaxResult close(@PathVariable("id") Long id) {
        int rows = tobAlertTaskService.closeTask(id, getUsername());
        return rows > 0 ? success() : error("操作失败");
    }

    /**
     * 手动推送任务（重新推送给第一个绑定人员）
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:task:push')")
    @PutMapping("/push/{id}")
    public AjaxResult push(@PathVariable("id") Long id) {
        int rows = tobAlertTaskService.pushTask(id);
        return rows > 0 ? success() : error("推送失败，任务不存在或已关闭");
    }

    /**
     * 提交反馈
     */
    @PostMapping("/feedback")
    public AjaxResult submitFeedback(@RequestBody Map<String, Object> params) {
        Long taskId = params.get("taskId") != null ? Long.valueOf(params.get("taskId").toString()) : null;
        String userId = params.get("userId") != null ? params.get("userId").toString() : getUsername();
        Integer isMeetAlert = params.get("isMeetAlert") != null ? Integer.valueOf(params.get("isMeetAlert").toString()) : null;
        Integer isIllegal = params.get("isIllegal") != null ? Integer.valueOf(params.get("isIllegal").toString()) : null;
        String remark = params.get("remark") != null ? params.get("remark").toString() : null;

        if (taskId == null || isMeetAlert == null || isIllegal == null) {
            return error("参数不完整");
        }

        int rows = tobAlertTaskService.submitFeedback(taskId, userId, isMeetAlert, isIllegal, remark);
        return rows > 0 ? success() : error("操作失败，任务状态不正确");
    }
}
