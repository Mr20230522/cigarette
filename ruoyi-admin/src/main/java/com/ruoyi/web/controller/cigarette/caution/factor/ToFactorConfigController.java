package com.ruoyi.web.controller.cigarette.caution.factor;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ToFactorConfig;
import com.ruoyi.system.service.IToFactorConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/factor/config")
public class ToFactorConfigController extends BaseController {

    @Autowired
    private IToFactorConfigService factorConfigService;

    @GetMapping("/list")
    public TableDataInfo list(ToFactorConfig filter) {
        startPage();
        List<ToFactorConfig> list = factorConfigService.selectFactorConfigList(filter);
        return getDataTable(list);
    }

    @GetMapping("/{id}")
    public AjaxResult getInfo(@PathVariable Long id) {
        return AjaxResult.success(factorConfigService.selectFactorConfigById(id));
    }

    @PostMapping
    public AjaxResult add(@RequestBody ToFactorConfig factorConfig) {
        return toAjax(factorConfigService.insertFactorConfig(factorConfig));
    }

    @PutMapping
    public AjaxResult edit(@RequestBody ToFactorConfig factorConfig) {
        return toAjax(factorConfigService.updateFactorConfig(factorConfig));
    }

    @DeleteMapping("/{id}")
    public AjaxResult remove(@PathVariable Long id) {
        return toAjax(factorConfigService.deleteFactorConfigById(id));
    }
}
