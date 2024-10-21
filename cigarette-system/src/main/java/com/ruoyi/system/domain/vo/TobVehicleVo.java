package com.ruoyi.system.domain.vo;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.TobVehicle;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

import java.util.Date;

/**
 * 车辆对象 tob_vehicle
 *
 * @author cigarette
 * @date 2024-09-12
 */
public class TobVehicleVo extends TobVehicle
{
    /** 驾驶人id */
    @Excel(name = "驾驶人姓名")
    private String Name;

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("carId", getCarId())
                .append("licensePlate", getLicensePlate())
                .append("licensePlateType", getLicensePlateType())
                .append("carTypeId", getCarTypeId())
                .append("carColor", getCarColor())
                .append("occurrenceNumber", getOccurrenceNumber())
                .append("casesInvolved", getCasesInvolved())
                .append("carOwnerId", getCarOwnerId())
                .append("name", getName())
                .append("vehiclePurchaseDate", getVehiclePurchaseDate())
                .append("illegalStatus", getIllegalStatus())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("picture", getPicture())
                .toString();
    }
}
