package com.ruoyi.web.controller.cigarette.vehicle;

import java.util.List;
import javax.servlet.http.HttpServletResponse;

import com.ruoyi.system.domain.vo.TobVehicleBehaviorVo;
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
import com.ruoyi.system.domain.TobVehicleBehavior;
import com.ruoyi.system.service.ITobVehicleBehaviorService;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.common.core.page.TableDataInfo;

/**
 * 车辆行为记录Controller
 *
 * @author cigarette
 * @date 2024-07-27
 */
@RestController
@RequestMapping("/cigarette/vehicle/vehicleBehavior")
public class TobVehicleBehaviorController extends BaseController
{
    @Autowired
    private ITobVehicleBehaviorService tobVehicleBehaviorService;

    /**
     * 查询车辆行为记录列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:list')")
    @GetMapping("/list")
    public TableDataInfo list(TobVehicleBehavior tobVehicleBehavior)
    {
        startPage();
        List<TobVehicleBehavior> list = tobVehicleBehaviorService.selectTobVehicleBehaviorList(tobVehicleBehavior);
        return getDataTable(list);
    }

    /**
     * 导出车辆行为记录列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:export')")
    @Log(title = "车辆行为记录", businessType = BusinessType.EXPORT)
    @PostMapping("/export")
    public void export(HttpServletResponse response, TobVehicleBehavior tobVehicleBehavior)
    {
        List<TobVehicleBehavior> list = tobVehicleBehaviorService.selectTobVehicleBehaviorList(tobVehicleBehavior);
        ExcelUtil<TobVehicleBehavior> util = new ExcelUtil<TobVehicleBehavior>(TobVehicleBehavior.class);
        util.exportExcel(response, list, "车辆行为记录数据");
    }

    /**
     * 获取车辆行为记录列表
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:list')")
    @GetMapping("/listVo")
    public TableDataInfo list(TobVehicleBehaviorVo tobVehicleBehaviorVo)
    {
        startPage();
        List<TobVehicleBehaviorVo> list = tobVehicleBehaviorService.selectTobVehicleBehaviorVoList(tobVehicleBehaviorVo);
        return getDataTable(list);
    }
    /**
     * 获取车辆行为记录详细信息
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:query')")
    @GetMapping(value = "/{behaviorId}")
    public AjaxResult getInfo(@PathVariable("behaviorId") Long behaviorId)
    {
        return success(tobVehicleBehaviorService.selectTobVehicleBehaviorByBehaviorId(behaviorId));
    }

    /**
     * 新增车辆行为记录
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:add')")
    @Log(title = "车辆行为记录", businessType = BusinessType.INSERT)
    @PostMapping
    public AjaxResult add(@RequestBody TobVehicleBehavior tobVehicleBehavior)
    {
        return toAjax(tobVehicleBehaviorService.insertTobVehicleBehavior(tobVehicleBehavior));
    }

    /**
     * 修改车辆行为记录
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:edit')")
    @Log(title = "车辆行为记录", businessType = BusinessType.UPDATE)
    @PutMapping
    public AjaxResult edit(@RequestBody TobVehicleBehavior tobVehicleBehavior)
    {
        return toAjax(tobVehicleBehaviorService.updateTobVehicleBehavior(tobVehicleBehavior));
    }

    /**
     * 删除车辆行为记录
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:remove')")
    @Log(title = "车辆行为记录", businessType = BusinessType.DELETE)
	@DeleteMapping("/{behaviorIds}")
    public AjaxResult remove(@PathVariable Long[] behaviorIds)
    {
        return toAjax(tobVehicleBehaviorService.deleteTobVehicleBehaviorByBehaviorIds(behaviorIds));
    }
}
