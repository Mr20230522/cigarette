package com.ruoyi.web.controller.cigarette.caution.retrieveSuspectHistory;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.SuspectVehicleQueryVO;
import com.ruoyi.system.service.ISuspectVehicleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/suspectVehicle")
public class SuspectVehicleQueryController extends BaseController {

    @Autowired
    private ISuspectVehicleQueryService suspectVehicleQueryService;

    @Log(title = "嫌疑车辆查询", businessType = BusinessType.OTHER)
    @GetMapping("/list")
    public TableDataInfo list(SuspectVehicleQueryVO vo) {
        // System.out.println("startTime: " + vo.getStartTime());
        // System.out.println("endTime: " + vo.getEndTime());
        // 启动分页（自动读取 pageNum / pageSize）
        startPage();

        // 查询数据
        List<ToVehicleRealTimMonitoring> list = suspectVehicleQueryService.selectSuspectVehicleList(vo);

        // 包装成分页结果（BaseController 提供）
        return getDataTable(list);
    }
}