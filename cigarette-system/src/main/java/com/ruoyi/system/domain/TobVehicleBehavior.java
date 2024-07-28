package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆行为记录对象 tob_vehicle_behavior
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public class TobVehicleBehavior extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 车行为id */
    private Long behaviorId;

    /** 车id */
    @Excel(name = "车id")
    private Long carId;

    /** 车型 */
    @Excel(name = "车型")
    private Long carTypeId;

    /** 车的颜色 */
    @Excel(name = "车的颜色")
    private String carColor;

    /** 嫌疑程度 */
    @Excel(name = "嫌疑程度")
    private Long degreeSuspicion;

    /** 驾驶人id */
    @Excel(name = "驾驶人id")
    private Long driverId;

    /** 行驶方向 */
    @Excel(name = "行驶方向")
    private String drivingDirection;

    /** 违法状态 */
    @Excel(name = "违法状态")
    private String illegalStatus;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;

    /** 同伙id */
    @Excel(name = "同伙id")
    private String accompliceId;

    public void setBehaviorId(Long behaviorId) 
    {
        this.behaviorId = behaviorId;
    }

    public Long getBehaviorId() 
    {
        return behaviorId;
    }
    public void setCarId(Long carId) 
    {
        this.carId = carId;
    }

    public Long getCarId() 
    {
        return carId;
    }
    public void setCarTypeId(Long carTypeId) 
    {
        this.carTypeId = carTypeId;
    }

    public Long getCarTypeId() 
    {
        return carTypeId;
    }
    public void setCarColor(String carColor) 
    {
        this.carColor = carColor;
    }

    public String getCarColor() 
    {
        return carColor;
    }
    public void setDegreeSuspicion(Long degreeSuspicion) 
    {
        this.degreeSuspicion = degreeSuspicion;
    }

    public Long getDegreeSuspicion() 
    {
        return degreeSuspicion;
    }
    public void setDriverId(Long driverId) 
    {
        this.driverId = driverId;
    }

    public Long getDriverId() 
    {
        return driverId;
    }
    public void setDrivingDirection(String drivingDirection) 
    {
        this.drivingDirection = drivingDirection;
    }

    public String getDrivingDirection() 
    {
        return drivingDirection;
    }
    public void setIllegalStatus(String illegalStatus) 
    {
        this.illegalStatus = illegalStatus;
    }

    public String getIllegalStatus() 
    {
        return illegalStatus;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setAccompliceId(String accompliceId) 
    {
        this.accompliceId = accompliceId;
    }

    public String getAccompliceId() 
    {
        return accompliceId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("behaviorId", getBehaviorId())
            .append("carId", getCarId())
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
            .toString();
    }
}
