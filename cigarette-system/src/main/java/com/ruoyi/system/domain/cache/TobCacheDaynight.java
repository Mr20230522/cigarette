package com.ruoyi.system.domain.cache;

import lombok.Data;
import java.util.Date;

@Data
public class TobCacheDaynight {
    private Long id;
    private Integer timeType;
    private Long dayNum;
    private Long nightNum;
    private Date updateTime;
}
