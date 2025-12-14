package com.ruoyi.system.domain.cache;

import lombok.Data;
import java.util.Date;

@Data
public class TobCacheVehicleType {
    private Long id;
    private Integer timeType;
    private Long sedan;
    private Long suvMpv;        // 对应中文列值 "SUV/MPV"
    private Long truck;
    private Long twoWheeler;
    private Long van;
    private Long lightTruck;
    private Long threeWheeler;
    private Long smallCar;
    private Long pedestrian;
    private Long largeVehicle;
    private Long pickup;
    private Long largeBus;
    private Long unknown;
    private Long nonMotor;
    private Long mediumBus;
    private Long other;
    private Date updateTime;
    private Long suvOnly;
}