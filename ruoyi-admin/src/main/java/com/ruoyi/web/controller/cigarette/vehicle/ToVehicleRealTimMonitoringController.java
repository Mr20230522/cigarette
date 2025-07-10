package com.ruoyi.web.controller.cigarette.vehicle;


import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.service.IToVehicleRealTimMonitoringService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/toVehicleMonitoring")
public class ToVehicleRealTimMonitoringController {

    //    @Autowired
//    private IToVehicleRealTimMonitoringService monitoringService;
//
//    @GetMapping("/list")
//    public AjaxResult list(ToVehicleRealTimMonitoring query) {
//        System.out.println("进入 list 接口了");
//        List<ToVehicleRealTimMonitoring> list = monitoringService.selectToVehicleRealTimMonitoringList(query);
//        return AjaxResult.success(list);
//    }
    @Autowired
    private IToVehicleRealTimMonitoringService vehicleService;

    @GetMapping("/list")
    public AjaxResult getNextRecord(@RequestParam(value = "lastId", defaultValue = "0") Long lastId) {
        ToVehicleRealTimMonitoring next = vehicleService.getNextRecord(lastId);
        if (next != null) {
            return AjaxResult.success(next);
        } else {
            return AjaxResult.error("没有更多数据");
        }
    }
}
