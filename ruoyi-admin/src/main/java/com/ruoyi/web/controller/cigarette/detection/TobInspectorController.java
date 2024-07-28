package com.ruoyi.web.controller.cigarette.detection;

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
import com.ruoyi.system.domain.TobInspector;
import com.ruoyi.system.service.ITobInspectorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 检测点人员Controller
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@RestController
@RequestMapping("/cigarette/detection/inspector")
public class TobInspectorController extends BaseController
{
    @Autowired
    private ITobInspectorService tobInspectorService;

    /**
     * 查询检测点人员列表
     */
    @PreAuthorize("@ss.hasPermi('detection:inspector:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobInspector tobInspector)
    {
        startPage();
        List<TobInspector> list = tobInspectorService.selectTobInspectorList(tobInspector);
        return getDataTable(list);
    }

    /**
     * 导出检测点人员列表
     */
    @PreAuthorize("@ss.hasPermi('detection:inspector:export')")
    @Log(title = "检测点人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobInspector tobInspector)
    {
        List<TobInspector> list = tobInspectorService.selectTobInspectorList(tobInspector);
        ExcelUtil<TobInspector> util = new ExcelUtil<TobInspector>(TobInspector.class);
        util.exportExcel(response, list, "检测点人员数据");
    }

    /**
     * 获取检测点人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('detection:inspector:query')")
    @GetMapping(value = "/{inspectorId}")
    public AjaxResult getInfo(@PathVariable("inspectorId") Long inspectorId)
    {
        return success(tobInspectorService.selectTobInspectorByInspectorId(inspectorId));
    }

    /**
     * 新增检测点人员
     */
    @PreAuthorize("@ss.hasPermi('detection:inspector:add')")
    @Log(title = "检测点人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobInspector tobInspector)
    {
        return toAjax(tobInspectorService.insertTobInspector(tobInspector));
    }

    /**
     * 修改检测点人员
     */
    @PreAuthorize("@ss.hasPermi('detection:inspector:edit')")
    @Log(title = "检测点人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobInspector tobInspector)
    {
        return toAjax(tobInspectorService.updateTobInspector(tobInspector));
    }

    /**
     * 删除检测点人员
     */
    @PreAuthorize("@ss.hasPermi('detection:inspector:remove')")
    @Log(title = "检测点人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{inspectorIds}")
    public AjaxResult remove(@PathVariable Long[] inspectorIds)
    {
        return toAjax(tobInspectorService.deleteTobInspectorByInspectorIds(inspectorIds));
    }
}
