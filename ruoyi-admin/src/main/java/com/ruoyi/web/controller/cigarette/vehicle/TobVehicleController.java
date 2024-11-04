package com.ruoyi.web.controller.cigarette.vehicle;

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
import com.ruoyi.system.domain.TobVehicle;
import com.ruoyi.system.service.ITobVehicleService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆Controller
 * 
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/vehicle/vehicle")
public class TobVehicleController extends BaseController
{
    @Autowired
    private ITobVehicleService tobVehicleService;

    /**
     * 查询车辆列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobVehicle tobVehicle)
    {
        startPage();
        List<TobVehicle> list = tobVehicleService.selectTobVehicleList(tobVehicle);
        return getDataTable(list);
    }

    /**
     * 查询新增车辆列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:list')")
    @GetMapping("/insertlist")
    public TableDataInfo insertlist(TobVehicle tobVehicle)
    {
        startPage();
        List<TobVehicle> list = tobVehicleService.selectTobVehicleInsertList(tobVehicle);
        return getDataTable(list);
    }

    /**
     * 导出车辆列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:export')")
    @Log(title = "车辆", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobVehicle tobVehicle)
    {
        List<TobVehicle> list = tobVehicleService.selectTobVehicleList(tobVehicle);
        ExcelUtil<TobVehicle> util = new ExcelUtil<TobVehicle>(TobVehicle.class);
        util.exportExcel(response, list, "车辆数据");
    }

    /**
     * 获取车辆详细信息
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:query')")
    @GetMapping(value = "/{carId}")
    public AjaxResult getInfo(@PathVariable("carId") Long carId)
    {
        return success(tobVehicleService.selectTobVehicleByCarId(carId));
    }

    /**
     * 新增车辆
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:add')")
    @Log(title = "车辆", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobVehicle tobVehicle)
    {
        return toAjax(tobVehicleService.insertTobVehicle(tobVehicle));
    }

    /**
     * 修改车辆
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:edit')")
    @Log(title = "车辆", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobVehicle tobVehicle)
    {
        System.out.println();
        return toAjax(tobVehicleService.updateTobVehicle(tobVehicle));

    }

    /**
     * 删除车辆
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicle:remove')")
    @Log(title = "车辆", businessType = BusinessType.DELETE)
	@DeleteMapping("/{carIds}")
    public AjaxResult remove(@PathVariable Long[] carIds)
    {
        return toAjax(tobVehicleService.deleteTobVehicleByCarIds(carIds));
    }
}
