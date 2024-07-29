package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 执勤记录对象 tob_duty_log
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public class TobDutyLog extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 执勤ID */
    private Long dutyId;

    /** 检测人员id */
    @Excel(name = "检测人员id")
    private Long inspectorId;

    /** 执勤时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "执勤时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date dutyTime;

    /** 删除标记 */
    private String delFlag;

    /** 执勤有误 */
    @Excel(name = "执勤有误")
    private String flag;

    /** 图片id */
    @Excel(name = "图片id")
    private Long keyPictureId;

    /** 视频id */
    @Excel(name = "视频id")
    private Long videoId;

    /** 行为 */
    @Excel(name = "行为")
    private String behavior;

    /** 是否工作 */
    @Excel(name = "是否工作")
    private String workFlag;

    public void setDutyId(Long dutyId) 
    {
        this.dutyId = dutyId;
    }

    public Long getDutyId() 
    {
        return dutyId;
    }
    public void setInspectorId(Long inspectorId) 
    {
        this.inspectorId = inspectorId;
    }

    public Long getInspectorId() 
    {
        return inspectorId;
    }
    public void setDutyTime(Date dutyTime) 
    {
        this.dutyTime = dutyTime;
    }

    public Date getDutyTime() 
    {
        return dutyTime;
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
    public void setKeyPictureId(Long keyPictureId) 
    {
        this.keyPictureId = keyPictureId;
    }

    public Long getKeyPictureId() 
    {
        return keyPictureId;
    }
    public void setVideoId(Long videoId) 
    {
        this.videoId = videoId;
    }

    public Long getVideoId() 
    {
        return videoId;
    }
    public void setBehavior(String behavior) 
    {
        this.behavior = behavior;
    }

    public String getBehavior() 
    {
        return behavior;
    }
    public void setWorkFlag(String workFlag) 
    {
        this.workFlag = workFlag;
    }

    public String getWorkFlag() 
    {
        return workFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("dutyId", getDutyId())
            .append("inspectorId", getInspectorId())
            .append("dutyTime", getDutyTime())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .append("flag", getFlag())
            .append("keyPictureId", getKeyPictureId())
            .append("videoId", getVideoId())
            .append("behavior", getBehavior())
            .append("workFlag", getWorkFlag())
            .toString();
    }
}
