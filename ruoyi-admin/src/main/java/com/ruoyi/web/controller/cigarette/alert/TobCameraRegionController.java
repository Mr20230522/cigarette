package com.ruoyi.web.controller.cigarette.alert;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobCameraRegion;
import com.ruoyi.system.service.ITobCameraRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * 卡口地域映射Controller
 */
@RestController
@RequestMapping("/cigarette/alert/cameraRegion")
public class TobCameraRegionController extends BaseController {

    @Autowired
    private ITobCameraRegionService tobCameraRegionService;

    /**
     * 查询卡口地域映射列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:cameraRegion:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobCameraRegion tobCameraRegion) {
        startPage();
        return getDataTable(tobCameraRegionService.selectTobCameraRegionList(tobCameraRegion));
    }

    /**
     * 获取卡口地域映射详情
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:cameraRegion:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(tobCameraRegionService.selectTobCameraRegionById(id));
    }

    /**
     * 获取所有地域下拉选项（DISTINCT location_id + location_name）
     */
    @GetMapping("/locations")
    public AjaxResult locations() {
        return success(tobCameraRegionService.selectDistinctLocations());
    }

    /**
     * 新增卡口地域映射
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:cameraRegion:add')")
    @Log(title = "卡口地域映射", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobCameraRegion tobCameraRegion) {
        tobCameraRegion.setCreateBy(getUsername());
        return toAjax(tobCameraRegionService.insertTobCameraRegion(tobCameraRegion));
    }

    /**
     * 修改卡口地域映射
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:cameraRegion:edit')")
    @Log(title = "卡口地域映射", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobCameraRegion tobCameraRegion) {
        tobCameraRegion.setUpdateBy(getUsername());
        return toAjax(tobCameraRegionService.updateTobCameraRegion(tobCameraRegion));
    }

    /**
     * 删除卡口地域映射
     */
    @PreAuthorize("@ss.hasPermi('cigarette:alert:cameraRegion:remove')")
    @Log(title = "卡口地域映射", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids) {
        return toAjax(tobCameraRegionService.deleteTobCameraRegionByIds(ids));
    }
}
