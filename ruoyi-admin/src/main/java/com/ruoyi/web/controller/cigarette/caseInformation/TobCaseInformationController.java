package com.ruoyi.web.controller.cigarette.caseInformation;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.vo.TobCaseInformationVo;
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
import com.ruoyi.system.domain.TobCaseInformation;
import com.ruoyi.system.service.ITobCaseInformationService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 案件信息Controller
 * 
 * @author cigarette
 * @date 2024-07-28
 */
@RestController
@RequestMapping("/cigarette/case/caseInformation")
public class TobCaseInformationController extends BaseController
{
    @Autowired
    private ITobCaseInformationService tobCaseInformationService;

    /**
     * 查询案件信息列表
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobCaseInformation tobCaseInformation)
    {
        startPage();
        List<TobCaseInformation> list = tobCaseInformationService.selectTobCaseInformationList(tobCaseInformation);
        return getDataTable(list);
    }

    /**
     * 查询案件信息列表Vo
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:list')")
    @GetMapping("/listVo")
    public TableDataInfo list(TobCaseInformationVo tobCaseInformationVo)
    {
        startPage();
        List<TobCaseInformationVo> list = tobCaseInformationService.selectTobCaseInformationVoList(tobCaseInformationVo);
        System.out.println(list);
        return getDataTable(list);
    }

    /**
     * 导出案件信息列表
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:export')")
    @Log(title = "案件信息", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobCaseInformation tobCaseInformation)
    {
        List<TobCaseInformation> list = tobCaseInformationService.selectTobCaseInformationList(tobCaseInformation);
        ExcelUtil<TobCaseInformation> util = new ExcelUtil<TobCaseInformation>(TobCaseInformation.class);
        util.exportExcel(response, list, "案件信息数据");
    }

    /**
     * 获取案件信息详细信息
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:query')")
    @GetMapping(value = "/{caseId}")
    public AjaxResult getInfo(@PathVariable("caseId") Long caseId)
    {
        return success(tobCaseInformationService.selectTobCaseInformationByCaseId(caseId));
    }

    /**
     * 新增案件信息
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:add')")
    @Log(title = "案件信息", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobCaseInformation tobCaseInformation)
    {

        System.out.printf("tobCaseInformation");
        System.out.println(tobCaseInformation);
        return toAjax(tobCaseInformationService.insertTobCaseInformation(tobCaseInformation));
    }

    /**
     * 修改案件信息
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:edit')")
    @Log(title = "案件信息", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobCaseInformation tobCaseInformation)
    {
        return toAjax(tobCaseInformationService.updateTobCaseInformation(tobCaseInformation));
    }

    /**
     * 删除案件信息
     */
    @PreAuthorize("@ss.hasPermi('case:caseInformation:remove')")
    @Log(title = "案件信息", businessType = BusinessType.DELETE)
	@DeleteMapping("/{caseIds}")
    public AjaxResult remove(@PathVariable Long[] caseIds)
    {
        return toAjax(tobCaseInformationService.deleteTobCaseInformationByCaseIds(caseIds));
    }
}
