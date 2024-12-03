package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 出勤记录对象 tob_clock_log
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public class TobClockLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 出勤ID */
    private Long commutingId;

    /** 检测人员id */
    @Excel(name = "检测人员id")
    private Long staffId;

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

    /** 值班类型 */
    @Excel(name = "值班类型")
    private String dutyType;

    /** 删除标记 */
    private String delFlag;

    /** 出勤有误 */
    @Excel(name = "出勤有误")
    private String flag;

    /**
     * 监测区域id
     */
    @Excel(name = "监测区域id")
    private Long detectionId;

    /**
     * 地区id
     */
    @Excel(name = "地区id")
    private Long districtId;

    public Long getDetectionId() {
        return detectionId;
    }

    public void setDetectionId(Long detectionId) {
        this.detectionId = detectionId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public void setCommutingId(Long commutingId)
    {
        this.commutingId = commutingId;
    }

    public Long getCommutingId() 
    {
        return commutingId;
    }

    public Long getStaffId() {
        return staffId;
    }

    public void setStaffId(Long staffId) {
        this.staffId = staffId;
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
    public void setDutyType(String dutyType) 
    {
        this.dutyType = dutyType;
    }

    public String getDutyType() 
    {
        return dutyType;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setFlag(String flag) 
    {
        this.flag = flag;
    }

    public String getFlag() 
    {
        return flag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("commutingId", getCommutingId())
                .append("staffId", getStaffId())
            .append("dutyDate", getDutyDate())
            .append("startTime", getStartTime())
            .append("endTime", getEndTime())
            .append("dutyType", getDutyType())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("flag", getFlag())
                .append("detectionId", getDetectionId())
                .append("districtId", getDistrictId())
            .toString();
    }
}
