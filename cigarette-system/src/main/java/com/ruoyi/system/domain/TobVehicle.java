package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆对象 tob_vehicle
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public class TobVehicle extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 车id */
    private Long carId;

    /** 车牌 */
    @Excel(name = "车牌")
    private String licensePlate;

    /** 车牌类型 */
    @Excel(name = "车牌类型")
    private Long licensePlateType;

    /** 车型 */
    @Excel(name = "车型")
    private Long carTypeId;

    /** 车的颜色 */
    @Excel(name = "车的颜色")
    private String carColor;

    /** 出现次数 */
    @Excel(name = "出现次数")
    private Long occurrenceNumber;

    /** 涉案数 */
    @Excel(name = "涉案数")
    private Long casesInvolved;

    /** 车主id */
    @Excel(name = "车主id")
    private Long carOwnerId;

    /** 车辆购置日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "车辆购置日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date vehiclePurchaseDate;

    /** 违法状态 */
    @Excel(name = "违法状态")
    private String illegalStatus;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;

    public void setCarId(Long carId) 
    {
        this.carId = carId;
    }

    public Long getCarId() 
    {
        return carId;
    }
    public void setLicensePlate(String licensePlate) 
    {
        this.licensePlate = licensePlate;
    }

    public String getLicensePlate() 
    {
        return licensePlate;
    }
    public void setLicensePlateType(Long licensePlateType) 
    {
        this.licensePlateType = licensePlateType;
    }

    public Long getLicensePlateType() 
    {
        return licensePlateType;
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
    public void setOccurrenceNumber(Long occurrenceNumber) 
    {
        this.occurrenceNumber = occurrenceNumber;
    }

    public Long getOccurrenceNumber() 
    {
        return occurrenceNumber;
    }
    public void setCasesInvolved(Long casesInvolved) 
    {
        this.casesInvolved = casesInvolved;
    }

    public Long getCasesInvolved() 
    {
        return casesInvolved;
    }
    public void setCarOwnerId(Long carOwnerId) 
    {
        this.carOwnerId = carOwnerId;
    }

    public Long getCarOwnerId() 
    {
        return carOwnerId;
    }
    public void setVehiclePurchaseDate(Date vehiclePurchaseDate) 
    {
        this.vehiclePurchaseDate = vehiclePurchaseDate;
    }

    public Date getVehiclePurchaseDate() 
    {
        return vehiclePurchaseDate;
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
            .append("vehiclePurchaseDate", getVehiclePurchaseDate())
            .append("illegalStatus", getIllegalStatus())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
