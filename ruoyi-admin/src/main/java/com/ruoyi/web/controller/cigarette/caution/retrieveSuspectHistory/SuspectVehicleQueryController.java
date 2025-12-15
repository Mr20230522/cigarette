package com.ruoyi.web.controller.cigarette.caution.retrieveSuspectHistory;

import com.ruoyi.common.annotation.Log;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.common.enums.BusinessType;
import com.ruoyi.common.utils.bean.BeanUtils;
import com.ruoyi.common.utils.poi.ExcelUtil;
import com.ruoyi.system.domain.ToVehicleRealTimMonitoring;
import com.ruoyi.system.domain.vo.SuspectVehicleExportVO;
import com.ruoyi.system.domain.vo.SuspectVehicleQueryVO;
import com.ruoyi.system.service.ITobSuspectVehicleQueryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/suspectVehicle")
public class SuspectVehicleQueryController extends BaseController {

    @Autowired
    private ITobSuspectVehicleQueryService suspectVehicleQueryService;

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

    /**
     * 导出嫌疑车辆数据
     */
    @Log(title = "嫌疑车辆导出", businessType = BusinessType.EXPORT)
    @GetMapping("/export")
    public void exportData(HttpServletResponse response, SuspectVehicleQueryVO queryVO) throws IOException {
        // 1. 查询数据（不分页）
        List<ToVehicleRealTimMonitoring> list = suspectVehicleQueryService.selectSuspectVehicleList(queryVO);

        // 2. 转换为导出对象
        List<SuspectVehicleExportVO> exportList = list.stream().map(entity -> {
            SuspectVehicleExportVO vo = new SuspectVehicleExportVO();
            BeanUtils.copyProperties(entity, vo); // 自动复制同名属性
            return vo;
        }).collect(Collectors.toList());

        // 3. 设置响应头
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String fileName = "suspect_vehicle_" + System.currentTimeMillis() + ".xlsx";
        response.setHeader("Content-disposition", "attachment;filename=" + fileName);

        // 4. 导出Excel
        ExcelUtil<SuspectVehicleExportVO> excelUtil = new ExcelUtil<>(SuspectVehicleExportVO.class);
        excelUtil.exportExcel(response, exportList, "嫌疑车辆数据", "数据");
    }
}