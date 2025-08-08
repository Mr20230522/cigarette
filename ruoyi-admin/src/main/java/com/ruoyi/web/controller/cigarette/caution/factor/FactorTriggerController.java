package com.ruoyi.web.controller.cigarette.caution.factor;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.FactorTrigger;
import com.ruoyi.system.service.IFactorTriggerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factor/trigger")
public class FactorTriggerController extends BaseController {

    @Autowired
    private IFactorTriggerService factorTriggerService;

    @GetMapping("/list")
    public TableDataInfo list(FactorTrigger filter) {
        startPage();
        List<FactorTrigger> list = factorTriggerService.selectFactorTriggerList(filter);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(factorTriggerService.selectFactorTriggerById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody FactorTrigger factorTrigger) {
        return toAjax(factorTriggerService.insertFactorTrigger(factorTrigger));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody FactorTrigger factorTrigger) {
        return toAjax(factorTriggerService.updateFactorTrigger(factorTrigger));
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(factorTriggerService.deleteFactorTriggerById(id));
    }
}
