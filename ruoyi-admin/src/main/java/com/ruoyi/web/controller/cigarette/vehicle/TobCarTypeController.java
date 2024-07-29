package com.ruoyi.web.controller.cigarette.vehicle;

import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.TobCarType;
import com.ruoyi.system.service.ITobCarTypeService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车型Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/vehicle/carType")
public class TobCarTypeController extends BaseController
{
    @Autowired
    private ITobCarTypeService tobCarTypeService;

    /**
     * 查询车型列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:carType:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobCarType tobCarType)
    {
        startPage();
        List<TobCarType> list = tobCarTypeService.selectTobCarTypeList(tobCarType);
        return getDataTable(list);
    }

    /**
     * 导出车型列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:carType:export')")
    @Log(title = "车型", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobCarType tobCarType)
    {
        List<TobCarType> list = tobCarTypeService.selectTobCarTypeList(tobCarType);
        ExcelUtil<TobCarType> util = new ExcelUtil<TobCarType>(TobCarType.class);
        util.exportExcel(response, list, "车型数据");
    }

    /**
     * 获取车型详细信息
     */
    @PreAuthorize("@ss.hasPermi('vehicle:carType:query')")
    @GetMapping(value = "/{carTypeId}")
    public AjaxResult getInfo(@PathVariable("carTypeId") Long carTypeId)
    {
        return success(tobCarTypeService.selectTobCarTypeByCarTypeId(carTypeId));
    }

    /**
     * 新增车型
     */
    @PreAuthorize("@ss.hasPermi('vehicle:carType:add')")
    @Log(title = "车型", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobCarType tobCarType)
    {
        return toAjax(tobCarTypeService.insertTobCarType(tobCarType));
    }

    /**
     * 修改车型
     */
    @PreAuthorize("@ss.hasPermi('vehicle:carType:edit')")
    @Log(title = "车型", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobCarType tobCarType)
    {
        return toAjax(tobCarTypeService.updateTobCarType(tobCarType));
    }

    /**
     * 删除车型
     */
    @PreAuthorize("@ss.hasPermi('vehicle:carType:remove')")
    @Log(title = "车型", businessType = BusinessType.DELETE)
	@DeleteMapping("/{carTypeIds}")
    public AjaxResult remove(@PathVariable Long[] carTypeIds)
    {
        return toAjax(tobCarTypeService.deleteTobCarTypeByCarTypeIds(carTypeIds));
    }
}
