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
import com.ruoyi.system.domain.TobDetection;
import com.ruoyi.system.service.ITobDetectionService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 监测区域Controller
 *
 * @author ruoyi
 * @date 2024-07-24
 */
@RestController
@RequestMapping("/cigarette/detection/detection")
public class TobDetectionController extends BaseController
{
    @Autowired
    private ITobDetectionService tobDetectionService;

    /**
     * 查询监测区域列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:detection:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobDetection tobDetection)
    {
        startPage();
        List<TobDetection> list = tobDetectionService.selectTobDetectionList(tobDetection);
        return getDataTable(list);
    }

    /**
     * 根据地区数据查询监测区域列表
     */
    @PreAuthorize("@ss.hasPermi('cigarette:detection:listByDistrictId')")
    @GetMapping("/listByDistrictId")
    public TableDataInfo listByDistrictId(TobDetection tobDetection)
    {
        startPage();
        List<TobDetection> list = tobDetectionService.selectTobDetectionListByDistrictId(tobDetection);
        return getDataTable(list);
    }


    @PreAuthorize("@ss.hasPermi('cigarette:detection:export')")
    @Log(title = "监测区域", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobDetection tobDetection)
    {
        List<TobDetection> list = tobDetectionService.selectTobDetectionList(tobDetection);
        ExcelUtil<TobDetection> util = new ExcelUtil<TobDetection>(TobDetection.class);
        util.exportExcel(response, list, "监测区域数据");
    }

    /**
     * 获取监测区域详细信息
     */
    @PreAuthorize("@ss.hasPermi('cigarette:detection:query')")
    @GetMapping(value = "/{detectionId}")
    public AjaxResult getInfo(@PathVariable("detectionId") Long detectionId)
    {
        return success(tobDetectionService.selectTobDetectionByDetectionId(detectionId));
    }

    /**
     * 新增监测区域
     */
    @PreAuthorize("@ss.hasPermi('cigarette:detection:add')")
    @Log(title = "监测区域", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobDetection tobDetection)
    {
        return toAjax(tobDetectionService.insertTobDetection(tobDetection));
    }

    /**
     * 修改监测区域
     */
    @PreAuthorize("@ss.hasPermi('cigarette:detection:edit')")
    @Log(title = "监测区域", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobDetection tobDetection)
    {
        return toAjax(tobDetectionService.updateTobDetection(tobDetection));
    }

    /**
     * 删除监测区域
     */
    @PreAuthorize("@ss.hasPermi('cigarette:detection:remove')")
    @Log(title = "监测区域", businessType = BusinessType.DELETE)
	@DeleteMapping("/{detectionIds}")
    public AjaxResult remove(@PathVariable Long[] detectionIds)
    {
        return toAjax(tobDetectionService.deleteTobDetectionByDetectionIds(detectionIds));
    }
}
