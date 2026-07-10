package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobWxworkConfig;
import com.ruoyi.system.service.ITobWxworkConfigService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 企业微信配置Controller
 */
@RestController
@RequestMapping("/cigarette/alert/wxworkConfig")
public class TobWxworkConfigController extends BaseController {

    @Autowired
    private ITobWxworkConfigService tobWxworkConfigService;

    /**
     * 查询企微配置列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:wxworkConfig:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobWxworkConfig tobWxworkConfig) {
        startPage();
        return getDataTable(tobWxworkConfigService.selectTobWxworkConfigList(tobWxworkConfig));
    }

    /**
     * 获取企微配置详情
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:wxworkConfig:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tobWxworkConfigService.selectTobWxworkConfigById(id));
    }

    /**
     * 新增企微配置
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:wxworkConfig:add')")
    @Log(title = "企微配置", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobWxworkConfig tobWxworkConfig) {
        tobWxworkConfig.setCreateBy(getUsername());
        return toAjax(tobWxworkConfigService.insertTobWxworkConfig(tobWxworkConfig));
    }

    /**
     * 修改企微配置
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:wxworkConfig:edit')")
    @Log(title = "企微配置", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobWxworkConfig tobWxworkConfig) {
        tobWxworkConfig.setUpdateBy(getUsername());
        return toAjax(tobWxworkConfigService.updateTobWxworkConfig(tobWxworkConfig));
    }

    /**
     * 删除企微配置
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:wxworkConfig:remove')")
    @Log(title = "企微配置", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tobWxworkConfigService.deleteTobWxworkConfigByIds(ids));
    }
}
