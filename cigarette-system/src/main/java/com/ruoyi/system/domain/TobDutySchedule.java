package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 值班对象 tob_duty_schedule
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public class TobDutySchedule extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 值班ID */
    private Long dutyId;

    /** 值班表名称 */
    @Excel(name = "值班表名称")
    private String scheduleName;

    /** 值班日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "值班日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dutyDate;

    /** 开始时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "开始时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date startTime;

    /** 结束时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结束时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date endTime;

    /** 值班地点 */
    @Excel(name = "值班地点")
    private String dutyLocation;

    /** 联系方式 */
    @Excel(name = "联系方式")
    private String contactInfo;

    /** 部门ID */
    @Excel(name = "部门ID")
    private Long deptId;

    /** 值班类型 */
    @Excel(name = "值班类型")
    private String dutyType;

    public void setDutyId(Long dutyId) 
    {
        this.dutyId = dutyId;
    }

    public Long getDutyId() 
    {
        return dutyId;
    }
    public void setScheduleName(String scheduleName) 
    {
        this.scheduleName = scheduleName;
    }

    public String getScheduleName() 
    {
        return scheduleName;
    }
    public void setDutyDate(Date dutyDate) 
    {
        this.dutyDate = dutyDate;
    }

    public Date getDutyDate() 
    {
        return dutyDate;
    }
    public void setStartTime(Date startTime) 
    {
        this.startTime = startTime;
    }

    public Date getStartTime() 
    {
        return startTime;
    }
    public void setEndTime(Date endTime) 
    {
        this.endTime = endTime;
    }

    public Date getEndTime() 
    {
        return endTime;
    }
    public void setDutyLocation(String dutyLocation) 
    {
        this.dutyLocation = dutyLocation;
    }

    public String getDutyLocation() 
    {
        return dutyLocation;
    }
    public void setContactInfo(String contactInfo) 
    {
        this.contactInfo = contactInfo;
    }

    public String getContactInfo() 
    {
        return contactInfo;
    }
    public void setDeptId(Long deptId) 
    {
        this.deptId = deptId;
    }

    public Long getDeptId() 
    {
        return deptId;
    }
    public void setDutyType(String dutyType) 
    {
        this.dutyType = dutyType;
    }

    public String getDutyType() 
    {
        return dutyType;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dutyId", getDutyId())
            .append("scheduleName", getScheduleName())
            .append("dutyDate", getDutyDate())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("dutyLocation", getDutyLocation())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("contactInfo", getContactInfo())
            .append("deptId", getDeptId())
            .append("dutyType", getDutyType())
            .toString();
    }
}
