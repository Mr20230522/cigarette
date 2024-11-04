package com.ruoyi.system.controller;

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
import com.ruoyi.system.domain.TobFeedback;
import com.ruoyi.system.service.ITobFeedbackService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 问题意见反馈Controller
 *
 * @author ruoyi
 * @date 2024-09-25
 */
@RestController
@RequestMapping("/system/feedback")
public class TobFeedbackController extends BaseController
{
    @Autowired
    private ITobFeedbackService tobFeedbackService;

    /**
     * 查询问题意见反馈列表
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobFeedback tobFeedback)
    {
        startPage();
        List<TobFeedback> list = tobFeedbackService.selectTobFeedbackList(tobFeedback);
        return getDataTable(list);
    }

    /**
     * 导出问题意见反馈列表
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:export')")
    @Log(title = "问题意见反馈", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobFeedback tobFeedback)
    {
        List<TobFeedback> list = tobFeedbackService.selectTobFeedbackList(tobFeedback);
        ExcelUtil<TobFeedback> util = new ExcelUtil<TobFeedback>(TobFeedback.class);
        util.exportExcel(response, list, "问题意见反馈数据");
    }

    /**
     * 获取问题意见反馈详细信息
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tobFeedbackService.selectTobFeedbackById(id));
    }

    /**
     * 新增问题意见反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:add')")
    @Log(title = "问题意见反馈", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobFeedback tobFeedback)
    {
        return toAjax(tobFeedbackService.insertTobFeedback(tobFeedback));
    }

    /**
     * 修改问题意见反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:edit')")
    @Log(title = "问题意见反馈", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobFeedback tobFeedback)
    {
        return toAjax(tobFeedbackService.updateTobFeedback(tobFeedback));
    }

    /**
     * 删除问题意见反馈
     */
    @PreAuthorize("@ss.hasPermi('system:feedback:remove')")
    @Log(title = "问题意见反馈", businessType = BusinessType.DELETE)
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tobFeedbackService.deleteTobFeedbackByIds(ids));
    }
}
