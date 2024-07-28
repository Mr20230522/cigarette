package com.ruoyi.web.controller.cigarette.caution;

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
import com.ruoyi.system.domain.TobCaution;
import com.ruoyi.system.service.ITobCautionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 预警记录Controller
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@RestController
@RequestMapping("/cigarette/caution/cautionLog")
public class TobCautionController extends BaseController
{
    @Autowired
    private ITobCautionService tobCautionService;

    /**
     * 查询预警记录列表
     */
    @PreAuthorize("@ss.hasPermi('caution:cautionLog:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobCaution tobCaution)
    {
        startPage();
        List<TobCaution> list = tobCautionService.selectTobCautionList(tobCaution);
        return getDataTable(list);
    }

    /**
     * 导出预警记录列表
     */
    @PreAuthorize("@ss.hasPermi('caution:cautionLog:export')")
    @Log(title = "预警记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobCaution tobCaution)
    {
        List<TobCaution> list = tobCautionService.selectTobCautionList(tobCaution);
        ExcelUtil<TobCaution> util = new ExcelUtil<TobCaution>(TobCaution.class);
        util.exportExcel(response, list, "预警记录数据");
    }

    /**
     * 获取预警记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('caution:cautionLog:query')")
    @GetMapping(value = "/{cautionId}")
    public AjaxResult getInfo(@PathVariable("cautionId") Long cautionId)
    {
        return success(tobCautionService.selectTobCautionByCautionId(cautionId));
    }

    /**
     * 新增预警记录
     */
    @PreAuthorize("@ss.hasPermi('caution:cautionLog:add')")
    @Log(title = "预警记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobCaution tobCaution)
    {
        return toAjax(tobCautionService.insertTobCaution(tobCaution));
    }

    /**
     * 修改预警记录
     */
    @PreAuthorize("@ss.hasPermi('caution:cautionLog:edit')")
    @Log(title = "预警记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobCaution tobCaution)
    {
        return toAjax(tobCautionService.updateTobCaution(tobCaution));
    }

    /**
     * 删除预警记录
     */
    @PreAuthorize("@ss.hasPermi('caution:cautionLog:remove')")
    @Log(title = "预警记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cautionIds}")
    public AjaxResult remove(@PathVariable Long[] cautionIds)
    {
        return toAjax(tobCautionService.deleteTobCautionByCautionIds(cautionIds));
    }
}
