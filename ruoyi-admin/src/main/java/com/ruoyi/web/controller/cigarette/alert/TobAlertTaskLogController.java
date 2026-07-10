package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.TobAlertTaskLog;
import com.ruoyi.system.service.ITobAlertTaskLogService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 推送日志Controller
 */
@RestController
@RequestMapping("/cigarette/alert/pushLog")
public class TobAlertTaskLogController extends BaseController {

    @Autowired
    private ITobAlertTaskLogService tobAlertTaskLogService;

    /**
     * 查询推送日志列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:push:list')")
    @GetMapping("/list")
    @DataScope(deptAlias = "", userAlias = "")
    public TableDataInfo list(TobAlertTaskLog tobAlertTaskLog) {
        startPage();
        return getDataTable(tobAlertTaskLogService.selectTobAlertTaskLogList(tobAlertTaskLog));
    }

    /**
     * 根据任务ID查询日志
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:push:query')")
    @GetMapping("/byTask/{taskId}")
    public TableDataInfo listByTask(@PathVariable("taskId") Long taskId) {
        startPage();
        return getDataTable(tobAlertTaskLogService.selectTobAlertTaskLogByTaskId(taskId));
    }
}
