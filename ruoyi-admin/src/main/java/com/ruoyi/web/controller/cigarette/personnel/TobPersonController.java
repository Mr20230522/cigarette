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
import com.ruoyi.system.domain.TobPerson;
import com.ruoyi.system.service.ITobPersonService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 驾驶人员Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/personnel/person")
public class TobPersonController extends BaseController
{
    @Autowired
    private ITobPersonService tobPersonService;

    /**
     * 查询驾驶人员列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:person:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobPerson tobPerson)
    {
        startPage();
        List<TobPerson> list = tobPersonService.selectTobPersonList(tobPerson);
        return getDataTable(list);
    }

    /**
     * 导出驾驶人员列表
     */
    @PreAuthorize("@ss.hasPermi('personnel:person:export')")
    @Log(title = "驾驶人员", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobPerson tobPerson)
    {
        List<TobPerson> list = tobPersonService.selectTobPersonList(tobPerson);
        ExcelUtil<TobPerson> util = new ExcelUtil<TobPerson>(TobPerson.class);
        util.exportExcel(response, list, "驾驶人员数据");
    }

    /**
     * 获取驾驶人员详细信息
     */
    @PreAuthorize("@ss.hasPermi('personnel:person:query')")
    @GetMapping(value = "/{suspectId}")
    public AjaxResult getInfo(@PathVariable("suspectId") Long suspectId)
    {
        return success(tobPersonService.selectTobPersonBySuspectId(suspectId));
    }

    /**
     * 新增驾驶人员
     */
    @PreAuthorize("@ss.hasPermi('personnel:person:add')")
    @Log(title = "驾驶人员", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobPerson tobPerson)
    {
        return toAjax(tobPersonService.insertTobPerson(tobPerson));
    }

    /**
     * 修改驾驶人员
     */
    @PreAuthorize("@ss.hasPermi('personnel:person:edit')")
    @Log(title = "驾驶人员", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobPerson tobPerson)
    {
        return toAjax(tobPersonService.updateTobPerson(tobPerson));
    }

    /**
     * 删除驾驶人员
     */
    @PreAuthorize("@ss.hasPermi('personnel:person:remove')")
    @Log(title = "驾驶人员", businessType = BusinessType.DELETE)
	@DeleteMapping("/{suspectIds}")
    public AjaxResult remove(@PathVariable Long[] suspectIds)
    {
        return toAjax(tobPersonService.deleteTobPersonBySuspectIds(suspectIds));
    }
}
