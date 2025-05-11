package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.system.domain.TobVehicleBehavior;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车辆行为记录对象 tob_vehicle_behavior
 *
 * @author cigarette
 * @date 2024-08-4
 */
public class TobVehicleBehaviorVo extends TobVehicleBehavior {
    /** 车id */
    @Excel(name = "车牌")
    private String licensePlate;
    /** 驾驶人id */
    @Excel(name = "驾驶人姓名")
    private String driverName;

    @Excel(name = "车辆图片")
    private String picture; // 新增字段

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }
    public String getPicture() {
        return picture;
    }
    public void setPicture(String picture) {
        this.picture = picture;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("behaviorId", getBehaviorId())
                .append("carId", getCarId())
                .append("driverName", getDriverName())
                .append("licensePlate", getLicensePlate())
                .append("carTypeId", getCarTypeId())
                .append("carColor", getCarColor())
                .append("degreeSuspicion", getDegreeSuspicion())
                .append("driverId", getDriverId())
                .append("drivingDirection", getDrivingDirection())
                .append("illegalStatus", getIllegalStatus())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("accompliceId", getAccompliceId())
                .append("picture", getPicture())
                .toString();
    }

}
