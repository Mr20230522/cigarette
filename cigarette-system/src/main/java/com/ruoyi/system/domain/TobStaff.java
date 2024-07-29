package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 工作人员对象 tob_staff
 * 
 * @author ruoyi
 * @date 2024-07-25
 */
public class TobStaff extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 工作人员id */
    private Long staffId;

    /** 用户id */
    @Excel(name = "用户id")
    private Long userId;

    /** 人脸特征编码 */
    @Excel(name = "人脸特征编码")
    private String faceFeature;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    @Excel(name = "删除标记")
    private String delFlag;

    public void setStaffId(Long staffId) 
    {
        this.staffId = staffId;
    }

    public Long getStaffId() 
    {
        return staffId;
    }
    public void setUserId(Long userId) 
    {
        this.userId = userId;
    }

    public Long getUserId() 
    {
        return userId;
    }
    public void setFaceFeature(String faceFeature) 
    {
        this.faceFeature = faceFeature;
    }

    public String getFaceFeature() 
    {
        return faceFeature;
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
            .append("staffId", getStaffId())
            .append("userId", getUserId())
            .append("faceFeature", getFaceFeature())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
