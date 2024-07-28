package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车型对象 tob_car_type
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public class TobCarType extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 表id */
    private Long carTypeId;

    /** 车型名称 */
    @Excel(name = "车型名称")
    private String carTypeName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;

    public void setCarTypeId(Long carTypeId) 
    {
        this.carTypeId = carTypeId;
    }

    public Long getCarTypeId() 
    {
        return carTypeId;
    }
    public void setCarTypeName(String carTypeName) 
    {
        this.carTypeName = carTypeName;
    }

    public String getCarTypeName() 
    {
        return carTypeName;
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
            .append("carTypeId", getCarTypeId())
            .append("carTypeName", getCarTypeName())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
