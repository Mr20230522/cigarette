package com.ruoyi.web.controller.cigarette.caution.factor;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.system.domain.ToVehicleFieldScore;
import com.ruoyi.system.service.IToVehicleFieldScoreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 车辆字段评分Controller
 */
@RestController
@RequestMapping("/system/vehicleFieldScore")
public class ToVehicleFieldScoreController extends BaseController {

    @Autowired
    private IToVehicleFieldScoreService toVehicleFieldScoreService;

    /**
     * 查询车辆字段评分列表
     */
    @GetMapping("/list")
    public TableDataInfo list(ToVehicleFieldScore toVehicleFieldScore) {
        startPage();
        List<ToVehicleFieldScore> list = toVehicleFieldScoreService.selectToVehicleFieldScoreList(toVehicleFieldScore);
        return getDataTable(list);
    }

    /**
     * 获取车辆字段评分详细信息
     */
    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id) {
        return success(toVehicleFieldScoreService.selectToVehicleFieldScoreById(id));
    }

    /**
     * 根据trafficdataId获取评分信息
     */
    @GetMapping("/byTrafficDataId")  // 直接定义路径，不包含变量
    public AjaxResult getByTrafficDataId(@RequestParam Long trafficDataId) {  // 使用 @RequestParam 接收参数
        return success(toVehicleFieldScoreService.selectByTrafficdataId(trafficDataId));
    }

    /**
     * 新增车辆字段评分
     */
    @PostMapping
    public AjaxResult add(@RequestBody ToVehicleFieldScore toVehicleFieldScore) {
        return toAjax(toVehicleFieldScoreService.insertToVehicleFieldScore(toVehicleFieldScore));
    }

    /**
     * 修改车辆字段评分
     */
    @PutMapping
    public AjaxResult edit(@RequestBody ToVehicleFieldScore toVehicleFieldScore) {
        return toAjax(toVehicleFieldScoreService.updateToVehicleFieldScoreByTrafficdataId(toVehicleFieldScore));
    }

    /**
     * 删除车辆字段评分
     */
    @DeleteMapping("/{ids}")
    public AjaxResult remove(@PathVariable String[] ids) {
        return toAjax(toVehicleFieldScoreService.deleteToVehicleFieldScoreByIds(String.join(",", ids)));
    }
}
