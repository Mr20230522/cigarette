package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 监测区域对象 tob_detection
 * 
 * @author ruoyi
 * @date 2024-07-24
 */
public class TobDetection extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 监测区域id */
    private Long detectionId;

    /** 地区id */
    private Long districtId;

    /** 监测点顺序 */
    @Excel(name = "监测点顺序")
    private String orderNum;

    /** 监测点名称 */
    @Excel(name = "监测点名称")
    private String districtName;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    @Excel(name = "删除标记")
    private String delFlag;

    /** 负责人id */
    @Excel(name = "负责人id")
    private Long responsibleId;

    public void setDetectionId(Long detectionId) 
    {
        this.detectionId = detectionId;
    }

    public Long getDetectionId() 
    {
        return detectionId;
    }
    public void setDistrictId(Long districtId) 
    {
        this.districtId = districtId;
    }

    public Long getDistrictId() 
    {
        return districtId;
    }
    public void setOrderNum(String orderNum) 
    {
        this.orderNum = orderNum;
    }

    public String getOrderNum() 
    {
        return orderNum;
    }
    public void setDistrictName(String districtName) 
    {
        this.districtName = districtName;
    }

    public String getDistrictName() 
    {
        return districtName;
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
    public void setResponsibleId(Long responsibleId) 
    {
        this.responsibleId = responsibleId;
    }

    public Long getResponsibleId() 
    {
        return responsibleId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("detectionId", getDetectionId())
            .append("districtId", getDistrictId())
            .append("orderNum", getOrderNum())
            .append("districtName", getDistrictName())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("responsibleId", getResponsibleId())
            .toString();
    }
}
