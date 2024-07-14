package com.ruoyi.web.controller.cigarette;

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
import com.ruoyi.system.domain.TobDistrict;
import com.ruoyi.system.service.ITobDistrictService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 地区Controller
 * 
 * @author ruoyi
 * @date 2024-07-11
 */
@RestController
@RequestMapping("/system/district")
public class TobDistrictController extends BaseController
{
    @Autowired
    private ITobDistrictService tobDistrictService;

    /**
     * 查询地区列表
     */
    @PreAuthorize("@ss.hasPermi('system:district:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobDistrict tobDistrict)
    {
        startPage();
        List<TobDistrict> list = tobDistrictService.selectTobDistrictList(tobDistrict);
        return getDataTable(list);
    }

    /**
     * 导出地区列表
     */
    @PreAuthorize("@ss.hasPermi('system:district:export')")
    @Log(title = "地区", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobDistrict tobDistrict)
    {
        List<TobDistrict> list = tobDistrictService.selectTobDistrictList(tobDistrict);
        ExcelUtil<TobDistrict> util = new ExcelUtil<TobDistrict>(TobDistrict.class);
        util.exportExcel(response, list, "地区数据");
    }

    /**
     * 获取地区详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:district:query')")
    @GetMapping(value = "/{districtId}")
    public AjaxResult getInfo(@PathVariable("districtId") Long districtId)
    {
        return success(tobDistrictService.selectTobDistrictByDistrictId(districtId));
    }

    /**
     * 新增地区
     */
    @PreAuthorize("@ss.hasPermi('system:district:add')")
    @Log(title = "地区", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobDistrict tobDistrict)
    {
        return toAjax(tobDistrictService.insertTobDistrict(tobDistrict));
    }

    /**
     * 修改地区
     */
    @PreAuthorize("@ss.hasPermi('system:district:edit')")
    @Log(title = "地区", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobDistrict tobDistrict)
    {
        return toAjax(tobDistrictService.updateTobDistrict(tobDistrict));
    }

    /**
     * 删除地区
     */
    @PreAuthorize("@ss.hasPermi('system:district:remove')")
    @Log(title = "地区", businessType = BusinessType.DELETE)
	@DeleteMapping("/{districtIds}")
    public AjaxResult remove(@PathVariable Long[] districtIds)
    {
        return toAjax(tobDistrictService.deleteTobDistrictByDistrictIds(districtIds));
    }
}
