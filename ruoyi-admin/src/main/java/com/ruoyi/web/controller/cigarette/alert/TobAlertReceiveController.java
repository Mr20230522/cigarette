package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.annotation.DataScope;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobAlertReceive;
import com.ruoyi.system.service.ITobAlertReceiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * 预警接收API（外部系统推送入口 + 管理端CRUD）
 */
@RestController
@RequestMapping("/cigarette/alert/receive")
public class TobAlertReceiveController extends BaseController {

    @Autowired
    private ITobAlertReceiveService tobAlertReceiveService;

    /**
     * 外部系统推送预警数据
     * POST /cigarette/alert/receive/accept
     * Header: Authorization: Bearer <token>
     * Body: {"originalId": 123, "reason": "超速"}
     */
    @PostMapping("/accept")
    public AjaxResult accept(@RequestBody Map<String, Object> params) {
        Long originalId = params.get("originalId") != null ? Long.valueOf(params.get("originalId").toString()) : null;
        String reason = params.get("reason") != null ? params.get("reason").toString() : null;

        if (originalId == null || reason == null) {
            return error("参数不完整，需要originalId和reason");
        }

        try {
            Long receiveId = tobAlertReceiveService.receiveAlert(originalId, reason);
            Map<String, Object> result = new HashMap<>();
            result.put("receiveId", receiveId);
            result.put("message", "接收成功");
            return success(result);
        } catch (RuntimeException e) {
            return error(e.getMessage());
        }
    }

    /**
     * 查询预警接收记录列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:receive:list')")
    @GetMapping("/list")
    @DataScope(deptAlias = "", userAlias = "")
    public TableDataInfo list(TobAlertReceive tobAlertReceive) {
        startPage();
        return getDataTable(tobAlertReceiveService.selectTobAlertReceiveList(tobAlertReceive));
    }

    /**
     * 获取预警接收记录详情
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:receive:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tobAlertReceiveService.selectTobAlertReceiveById(id));
    }
}
