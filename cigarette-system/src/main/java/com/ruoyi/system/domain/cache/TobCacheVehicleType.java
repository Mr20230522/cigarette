package com.ruoyi.system.domain.cache;

import lombok.Data;
import java.util.Date;

@Data
public class TobCacheVehicleType {
    private Long id;
    private Integer timeType;
    private Long sedan;
    private Long suv;
    private Long mpv;
    private Long truck;
    private Long bus;
    private Long micro;
    private Long pickup;
    private Long trailer;
    private Long motor;
    private Long other;
    private Date updateTime;
}