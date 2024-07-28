package com.ruoyi.web.controller.cigarette.document;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.TobCaseInformation;
import com.ruoyi.system.service.ITobCaseInformationService;
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
import com.ruoyi.system.domain.TobDocument;
import com.ruoyi.system.service.ITobDocumentService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 文档Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/document/document")
public class TobDocumentController extends BaseController
{
    @Autowired
    private ITobDocumentService tobDocumentService;

    /**
     * 查询文档列表
     */
    @PreAuthorize("@ss.hasPermi('document:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobDocument tobDocument)
    {
        startPage();
        List<TobDocument> list = tobDocumentService.selectTobDocumentList(tobDocument);
        return getDataTable(list);
    }

    /**
     * 导出文档列表
     */
    @PreAuthorize("@ss.hasPermi('document:export')")
    @Log(title = "文档", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobDocument tobDocument)
    {
        List<TobDocument> list = tobDocumentService.selectTobDocumentList(tobDocument);
        ExcelUtil<TobDocument> util = new ExcelUtil<TobDocument>(TobDocument.class);
        util.exportExcel(response, list, "文档数据");
    }

    /**
     * 获取文档详细信息
     */
    @PreAuthorize("@ss.hasPermi('document:query')")
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        return success(tobDocumentService.selectTobDocumentById(id));
    }

    /**
     * 新增文档
     */
    @PreAuthorize("@ss.hasPermi('document:add')")
    @Log(title = "文档", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobDocument tobDocument)
    {
        return toAjax(tobDocumentService.insertTobDocument(tobDocument));
    }

    /**
     * 修改文档
     */
    @PreAuthorize("@ss.hasPermi('document:edit')")
    @Log(title = "文档", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobDocument tobDocument)
    {
        return toAjax(tobDocumentService.updateTobDocument(tobDocument));
    }

    /**
     * 删除文档
     */
    @PreAuthorize("@ss.hasPermi('document:remove')")
    @Log(title = "文档", businessType = BusinessType.DELETE)
	@DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        return toAjax(tobDocumentService.deleteTobDocumentByIds(ids));
    }
}
