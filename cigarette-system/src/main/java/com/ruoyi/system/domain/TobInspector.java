package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 检测点人员对象 tob_inspector
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public class TobInspector extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 检测人员id */
    private Long inspectorId;

    /** 检测点id */
    @Excel(name = "检测点id")
    private Long detectionId;

    /** 值班表id */
    @Excel(name = "值班表id")
    private Long dutyId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;

    /** 工作人员id */
    @Excel(name = "工作人员id")
    private Long staffId;

    public void setInspectorId(Long inspectorId) 
    {
        this.inspectorId = inspectorId;
    }

    public Long getInspectorId() 
    {
        return inspectorId;
    }
    public void setDetectionId(Long detectionId) 
    {
        this.detectionId = detectionId;
    }

    public Long getDetectionId() 
    {
        return detectionId;
    }
    public void setDutyId(Long dutyId) 
    {
        this.dutyId = dutyId;
    }

    public Long getDutyId() 
    {
        return dutyId;
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
    public void setStaffId(Long staffId) 
    {
        this.staffId = staffId;
    }

    public Long getStaffId() 
    {
        return staffId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("inspectorId", getInspectorId())
            .append("detectionId", getDetectionId())
            .append("dutyId", getDutyId())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("staffId", getStaffId())
            .toString();
    }
}
