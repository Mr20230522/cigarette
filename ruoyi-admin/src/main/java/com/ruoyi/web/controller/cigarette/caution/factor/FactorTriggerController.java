package com.ruoyi.web.controller.cigarette.caution.factor;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.FactorTriggerValue;
import com.ruoyi.system.service.IFactorTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factorTrigger")
public class FactorTriggerController extends BaseController {

    @Autowired
    private IFactorTriggerService factorTriggerService;

    /**
     * 查询触发值列表
     */
    @GetMapping("/list")
    public TableDataInfo list(FactorTriggerValue filter) {
        startPage();
        List<FactorTriggerValue> list = factorTriggerService.selectFactorTriggerValueList(filter);
        return getDataTable(list);
    }

    /**
     * 新增触发值
     */
    @Log(title = "触发值管理", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody FactorTriggerValue record) {
        return toAjax(factorTriggerService.insertFactorTriggerValue(record));
    }

    /**
     * 删除触发值
     */
    @Log(title = "触发值管理", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(factorTriggerService.deleteFactorTriggerValueByIds(ids));
    }
}

