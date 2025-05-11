package com.ruoyi.web.controller.cigarette.vehicle;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.vo.ToVehicleBehaviorMediaVo;
import com.ruoyi.system.service.IToVehicleBehaviorMediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 车辆行为媒体文件控制器
 */
@RestController
@RequestMapping("/cigarette/vehicle/vehicleBehaviorMedia")
public class ToVehicleBehaviorMediaController extends BaseController {

    @Autowired
    private IToVehicleBehaviorMediaService vehicleBehaviorService;

    /**
     * 获取车辆行为媒体文件信息
     */
    @PreAuthorize("@ss.hasPermi('vehicle:vehicleBehavior:query')")
    @GetMapping("/{behaviorId}")
    public AjaxResult getBehaviorMedia(@PathVariable("behaviorId") Long behaviorId) {
        ToVehicleBehaviorMediaVo mediaInfo = vehicleBehaviorService.getBehaviorMedia(behaviorId);
        System.out.println("mediaInfo:"+mediaInfo);
        return success(mediaInfo);
    }
}