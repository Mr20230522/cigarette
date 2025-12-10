package com.ruoyi.system.domain.cache;

import lombok.Data;
import java.util.Date;

@Data
public class TobCacheSeason {
    private Long id;
    private Integer timeType;
    private Long spring;
    private Long summer;
    private Long autumn;
    private Long winter;
    private Date updateTime;
}