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
import com.ruoyi.system.domain.TobClockLog;
import com.ruoyi.system.service.ITobClockLogService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 出勤记录Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/personnel/clockLog")
public class TobClockLogController extends BaseController
{
    @Autowired
    private ITobClockLogService tobClockLogService;

    /**
     * 查询出勤记录列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:clockLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobClockLog tobClockLog)
    {
        startPage();
        List<TobClockLog> list = tobClockLogService.selectTobClockLogList(tobClockLog);
        return getDataTable(list);
    }

    /**
     * 导出出勤记录列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:clockLog:export')")
    @Log(title = "出勤记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobClockLog tobClockLog)
    {
        List<TobClockLog> list = tobClockLogService.selectTobClockLogList(tobClockLog);
        ExcelUtil<TobClockLog> util = new ExcelUtil<TobClockLog>(TobClockLog.class);
        util.exportExcel(response, list, "出勤记录数据");
    }

    /**
     * 获取出勤记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('personnel:clockLog:query')")
    @GetMapping(value = "/{commutingId}")
    public AjaxResult getInfo(@PathVariable("commutingId") Long commutingId)
    {
        return success(tobClockLogService.selectTobClockLogByCommutingId(commutingId));
    }

    /**
     * 新增出勤记录
     */
    @PreAuthorize("@ss.hasPermi('personnel:clockLog:add')")
    @Log(title = "出勤记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobClockLog tobClockLog)
    {
        return toAjax(tobClockLogService.insertTobClockLog(tobClockLog));
    }

    /**
     * 修改出勤记录
     */
    @PreAuthorize("@ss.hasPermi('personnel:clockLog:edit')")
    @Log(title = "出勤记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobClockLog tobClockLog)
    {
        return toAjax(tobClockLogService.updateTobClockLog(tobClockLog));
    }

    /**
     * 删除出勤记录
     */
    @PreAuthorize("@ss.hasPermi('personnel:clockLog:remove')")
    @Log(title = "出勤记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{commutingIds}")
    public AjaxResult remove(@PathVariable Long[] commutingIds)
    {
        return toAjax(tobClockLogService.deleteTobClockLogByCommutingIds(commutingIds));
    }
}
