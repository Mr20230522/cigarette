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
import com.ruoyi.system.domain.TobDutySchedule;
import com.ruoyi.system.service.ITobDutyScheduleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 值班Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/personnel/schedule")
public class TobDutyScheduleController extends BaseController
{
    @Autowired
    private ITobDutyScheduleService tobDutyScheduleService;

    /**
     * 查询值班列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:schedule:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobDutySchedule tobDutySchedule)
    {
        startPage();
        List<TobDutySchedule> list = tobDutyScheduleService.selectTobDutyScheduleList(tobDutySchedule);
        return getDataTable(list);
    }

    /**
     * 导出值班列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:schedule:export')")
    @Log(title = "值班", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobDutySchedule tobDutySchedule)
    {
        List<TobDutySchedule> list = tobDutyScheduleService.selectTobDutyScheduleList(tobDutySchedule);
        ExcelUtil<TobDutySchedule> util = new ExcelUtil<TobDutySchedule>(TobDutySchedule.class);
        util.exportExcel(response, list, "值班数据");
    }

    /**
     * 获取值班详细信息
     */
    @PreAuthorize("@ss.hasPermi('personnel:schedule:query')")
    @GetMapping(value = "/{dutyId}")
    public AjaxResult getInfo(@PathVariable("dutyId") Long dutyId)
    {
        return success(tobDutyScheduleService.selectTobDutyScheduleByDutyId(dutyId));
    }

    /**
     * 新增值班
     */
    @PreAuthorize("@ss.hasPermi('personnel:schedule:add')")
    @Log(title = "值班", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobDutySchedule tobDutySchedule)
    {
        return toAjax(tobDutyScheduleService.insertTobDutySchedule(tobDutySchedule));
    }

    /**
     * 修改值班
     */
    @PreAuthorize("@ss.hasPermi('personnel:schedule:edit')")
    @Log(title = "值班", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobDutySchedule tobDutySchedule)
    {
        return toAjax(tobDutyScheduleService.updateTobDutySchedule(tobDutySchedule));
    }

    /**
     * 删除值班
     */
    @PreAuthorize("@ss.hasPermi('personnel:schedule:remove')")
    @Log(title = "值班", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dutyIds}")
    public AjaxResult remove(@PathVariable Long[] dutyIds)
    {
        return toAjax(tobDutyScheduleService.deleteTobDutyScheduleByDutyIds(dutyIds));
    }
}
