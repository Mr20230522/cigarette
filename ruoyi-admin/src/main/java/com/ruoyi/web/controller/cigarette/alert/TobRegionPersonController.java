package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobRegionPerson;
import com.ruoyi.system.service.ITobRegionPersonService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 地域人员绑定Controller
 */
@RestController
@RequestMapping("/cigarette/alert/regionPerson")
public class TobRegionPersonController extends BaseController {

    @Autowired
    private ITobRegionPersonService tobRegionPersonService;

    /**
     * 查询地域人员绑定列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:regionPerson:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobRegionPerson tobRegionPerson) {
        startPage();
        return getDataTable(tobRegionPersonService.selectTobRegionPersonList(tobRegionPerson));
    }

    /**
     * 获取地域人员绑定详情
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:regionPerson:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tobRegionPersonService.selectTobRegionPersonById(id));
    }

    /**
     * 新增地域人员绑定
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:regionPerson:add')")
    @Log(title = "地域人员绑定", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobRegionPerson tobRegionPerson) {
        tobRegionPerson.setCreateBy(getUsername());
        return toAjax(tobRegionPersonService.insertTobRegionPerson(tobRegionPerson));
    }

    /**
     * 修改地域人员绑定
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:regionPerson:edit')")
    @Log(title = "地域人员绑定", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobRegionPerson tobRegionPerson) {
        tobRegionPerson.setUpdateBy(getUsername());
        return toAjax(tobRegionPersonService.updateTobRegionPerson(tobRegionPerson));
    }

    /**
     * 删除地域人员绑定
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:regionPerson:remove')")
    @Log(title = "地域人员绑定", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tobRegionPersonService.deleteTobRegionPersonByIds(ids));
    }
}
