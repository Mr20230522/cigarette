package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import lombok.Data;

import java.util.Date;
@Data
public class TobSuspectVehicleExportVO {
    /** 相机名称 */
    @Excel(name = "相机名称")
    private String cameraName;

    /** 车道号 */
    @Excel(name = "车道号")
    private Integer laneNo;

    /** 方向 */
    @Excel(name = "方向")
    private String direction;

    /** 车速(km/h) */
    @Excel(name = "车速")
    private Integer speed;

    /** 车牌 */
    @Excel(name = "车牌")
    private String plate;

    /** 车牌类型 */
    @Excel(name = "车牌类型")
    private String plateType;

    /** 车牌颜色 */
    @Excel(name = "车牌颜色")
    private String plateColor;

    /** 车身颜色 */
    @Excel(name = "车身颜色")
    private String vehicleColor;

    /** 车辆类型 */
    @Excel(name = "车辆类型")
    private String vehicleType;

    /** 捕获时间 */
    @Excel(name = "捕获时间", width = 30, dateFormat = "yyyy-MM-dd HH:mm:ss")
    private Date captureTime;

    /** 嫌疑程度 */
    @Excel(name = "嫌疑程度")
    private Integer level;
}