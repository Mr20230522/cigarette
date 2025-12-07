package com.ruoyi.system.domain.vo;

import lombok.Data;

import java.util.Date;

/**
 * 嫌疑车辆查询条件
 */
@Data
public class SuspectVehicleQueryVO {
    private String plate;           // 车牌号码（模糊）
    private String plateType;       // 车牌类型
    private String plateColor;      // 车牌颜色
    private String vehicleType;     // 车辆类型
    private String vehicleColor;    // 车身颜色
    private Date startTime;         // 捕获开始时间
    private Date endTime;           // 捕获结束时间
    private Integer level;          // 嫌疑程度
}