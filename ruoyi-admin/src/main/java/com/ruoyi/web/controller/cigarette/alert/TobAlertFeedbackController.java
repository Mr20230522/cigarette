package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TobAlertFeedback;
import com.ruoyi.system.service.ITobAlertFeedbackService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 反馈管理Controller
 */
@RestController
@RequestMapping("/cigarette/alert/feedback")
public class TobAlertFeedbackController extends BaseController {

    @Autowired
    private ITobAlertFeedbackService tobAlertFeedbackService;

    /**
     * 查询反馈列表
     */
    @GetMapping("/list")
    public TableDataInfo list(TobAlertFeedback tobAlertFeedback) {
        startPage();
        return getDataTable(tobAlertFeedbackService.selectTobAlertFeedbackList(tobAlertFeedback));
    }

    /**
     * 根据任务ID获取反馈详情
     */
    @GetMapping("/byTask/{taskId}")
    public AjaxResult getInfoByTaskId(@PathVariable("taskId") Long taskId) {
        return success(tobAlertFeedbackService.selectTobAlertFeedbackByTaskId(taskId));
    }
}
