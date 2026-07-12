package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobCaseHistory;
import com.ruoyi.system.service.ITobCaseHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 历史案件Controller
 */
@RestController
@RequestMapping("/cigarette/alert/caseHistory")
public class TobCaseHistoryController extends BaseController {

    @Autowired
    private ITobCaseHistoryService tobCaseHistoryService;

    /**
     * 查询历史案件列表（支持车牌模糊搜索）
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:caseHistory:list')")
    @GetMapping("/list")
    public AjaxResult list(@RequestParam(required = false) String plate) {
        return success(tobCaseHistoryService.searchByPlate(plate));
    }

    /**
     * 获取历史案件详情
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:caseHistory:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tobCaseHistoryService.getById(id));
    }

    /**
     * 新增历史案件
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:caseHistory:add')")
    @Log(title = "历史案件", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobCaseHistory record) {
        return toAjax(tobCaseHistoryService.insert(record));
    }

    /**
     * 修改历史案件
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:caseHistory:edit')")
    @Log(title = "历史案件", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobCaseHistory record) {
        return toAjax(tobCaseHistoryService.update(record));
    }
}
