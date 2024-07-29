package com.ruoyi.web.controller.cigarette.personnel;

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
import com.ruoyi.system.domain.TobStaff;
import com.ruoyi.system.service.ITobStaffService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 工作人员Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/personnel/staff")
public class TobStaffController extends BaseController
{
    @Autowired
    private ITobStaffService tobStaffService;

    /**
     * 查询工作人员列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:staff:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobStaff tobStaff)
    {
        startPage();
        List<TobStaff> list = tobStaffService.selectTobStaffList(tobStaff);
        return getDataTable(list);
    }

    /**
     * 导出工作人员列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:staff:export')")
    @Log(title = "工作人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobStaff tobStaff)
    {
        List<TobStaff> list = tobStaffService.selectTobStaffList(tobStaff);
        ExcelUtil<TobStaff> util = new ExcelUtil<TobStaff>(TobStaff.class);
        util.exportExcel(response, list, "工作人员数据");
    }

    /**
     * 获取工作人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('personnel:staff:query')")
    @GetMapping(value = "/{staffId}")
    public AjaxResult getInfo(@PathVariable("staffId") Long staffId)
    {
        return success(tobStaffService.selectTobStaffByStaffId(staffId));
    }

    /**
     * 新增工作人员
     */
    @PreAuthorize("@ss.hasPermi('personnel:staff:add')")
    @Log(title = "工作人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobStaff tobStaff)
    {
        return toAjax(tobStaffService.insertTobStaff(tobStaff));
    }

    /**
     * 修改工作人员
     */
    @PreAuthorize("@ss.hasPermi('personnel:staff:edit')")
    @Log(title = "工作人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobStaff tobStaff)
    {
        return toAjax(tobStaffService.updateTobStaff(tobStaff));
    }

    /**
     * 删除工作人员
     */
    @PreAuthorize("@ss.hasPermi('personnel:staff:remove')")
    @Log(title = "工作人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{staffIds}")
    public AjaxResult remove(@PathVariable Long[] staffIds)
    {
        return toAjax(tobStaffService.deleteTobStaffByStaffIds(staffIds));
    }
}
