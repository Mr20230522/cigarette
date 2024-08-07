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
import com.ruoyi.system.domain.TobCamera;
import com.ruoyi.system.service.ITobCameraService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 摄像头Controller
 *
 * @author cigarette
 * @date 2024-07-28
 */
@RestController
@RequestMapping("/cigarette/detection/camera")
public class TobCameraController extends BaseController
{
    @Autowired
    private ITobCameraService tobCameraService;

    /**
     * 查询摄像头列表
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobCamera tobCamera)
    {
        startPage();
        List<TobCamera> list = tobCameraService.selectTobCameraList(tobCamera);
        return getDataTable(list);
    }
    /**
     * 根据地区数据查询摄像头列表
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:listByDistrictId')")
    @GetMapping("/listByDistrictId")
    public TableDataInfo listByDistrictId(TobCamera tobCamera)
    {
        startPage();
        List<TobCamera> list = tobCameraService.selectTobCameraListByDistrictId(tobCamera);
        return getDataTable(list);
    }
    /**
     * 导出摄像头列表
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:export')")
    @Log(title = "摄像头", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobCamera tobCamera)
    {
        List<TobCamera> list = tobCameraService.selectTobCameraList(tobCamera);
        ExcelUtil<TobCamera> util = new ExcelUtil<TobCamera>(TobCamera.class);
        util.exportExcel(response, list, "摄像头数据");
    }

    /**
     * 获取摄像头详细信息
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:query')")
    @GetMapping(value = "/{cameraId}")
    public AjaxResult getInfo(@PathVariable("cameraId") Long cameraId)
    {
        return success(tobCameraService.selectTobCameraByCameraId(cameraId));
    }

    /**
     * 新增摄像头
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:add')")
    @Log(title = "摄像头", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobCamera tobCamera)
    {
        return toAjax(tobCameraService.insertTobCamera(tobCamera));
    }

    /**
     * 修改摄像头
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:edit')")
    @Log(title = "摄像头", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobCamera tobCamera)
    {
        return toAjax(tobCameraService.updateTobCamera(tobCamera));
    }

    /**
     * 删除摄像头
     */
    @PreAuthorize("@ss.hasPermi('detection:camera:remove')")
    @Log(title = "摄像头", businessType = BusinessType.DELETE)
	@DeleteMapping("/{cameraIds}")
    public AjaxResult remove(@PathVariable Long[] cameraIds)
    {
        return toAjax(tobCameraService.deleteTobCameraByCameraIds(cameraIds));
    }
}
