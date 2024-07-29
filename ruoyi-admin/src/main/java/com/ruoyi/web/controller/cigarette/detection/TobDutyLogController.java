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
import com.ruoyi.system.domain.TobDutyLog;
import com.ruoyi.system.service.ITobDutyLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 执勤记录Controller
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@RestController
@RequestMapping("/cigarette/detection/dutyLog")
public class TobDutyLogController extends BaseController
{
    @Autowired
    private ITobDutyLogService tobDutyLogService;

    /**
     * 查询执勤记录列表
     */
    @PreAuthorize("@ss.hasPermi('detection:dutyLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobDutyLog tobDutyLog)
    {
        startPage();
        List<TobDutyLog> list = tobDutyLogService.selectTobDutyLogList(tobDutyLog);
        return getDataTable(list);
    }

    /**
     * 导出执勤记录列表
     */
    @PreAuthorize("@ss.hasPermi('detection:dutyLog:export')")
    @Log(title = "执勤记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobDutyLog tobDutyLog)
    {
        List<TobDutyLog> list = tobDutyLogService.selectTobDutyLogList(tobDutyLog);
        ExcelUtil<TobDutyLog> util = new ExcelUtil<TobDutyLog>(TobDutyLog.class);
        util.exportExcel(response, list, "执勤记录数据");
    }

    /**
     * 获取执勤记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('detection:dutyLog:query')")
    @GetMapping(value = "/{dutyId}")
    public AjaxResult getInfo(@PathVariable("dutyId") Long dutyId)
    {
        return success(tobDutyLogService.selectTobDutyLogByDutyId(dutyId));
    }

    /**
     * 新增执勤记录
     */
    @PreAuthorize("@ss.hasPermi('detection:dutyLog:add')")
    @Log(title = "执勤记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobDutyLog tobDutyLog)
    {
        return toAjax(tobDutyLogService.insertTobDutyLog(tobDutyLog));
    }

    /**
     * 修改执勤记录
     */
    @PreAuthorize("@ss.hasPermi('detection:dutyLog:edit')")
    @Log(title = "执勤记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobDutyLog tobDutyLog)
    {
        return toAjax(tobDutyLogService.updateTobDutyLog(tobDutyLog));
    }

    /**
     * 删除执勤记录
     */
    @PreAuthorize("@ss.hasPermi('detection:dutyLog:remove')")
    @Log(title = "执勤记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{dutyIds}")
    public AjaxResult remove(@PathVariable Long[] dutyIds)
    {
        return toAjax(tobDutyLogService.deleteTobDutyLogByDutyIds(dutyIds));
    }
}
