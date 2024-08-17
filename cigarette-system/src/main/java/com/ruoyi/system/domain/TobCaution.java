package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 预警记录对象 tob_caution
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public class TobCaution extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 警示记录id */
    private Long cautionId;

    /** 监测点id */
    @Excel(name = "监测点id")
    private Long detectionId;

    /** 车行为id */
    @Excel(name = "车行为id")
    private Long behaviorId;

    /** 警示效果 */
    @Excel(name = "警示效果")
    private String warningEffect;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;


    /**
     * 地区id
     */
    @Excel(name = "地区id")
    private Long districtId;

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public void setCautionId(Long cautionId)
    {
        this.cautionId = cautionId;
    }

    public Long getCautionId() 
    {
        return cautionId;
    }
    public void setDetectionId(Long detectionId) 
    {
        this.detectionId = detectionId;
    }

    public Long getDetectionId() 
    {
        return detectionId;
    }
    public void setBehaviorId(Long behaviorId) 
    {
        this.behaviorId = behaviorId;
    }

    public Long getBehaviorId() 
    {
        return behaviorId;
    }
    public void setWarningEffect(String warningEffect) 
    {
        this.warningEffect = warningEffect;
    }

    public String getWarningEffect() 
    {
        return warningEffect;
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
            .append("cautionId", getCautionId())
            .append("detectionId", getDetectionId())
            .append("behaviorId", getBehaviorId())
            .append("warningEffect", getWarningEffect())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
                .append("districtId", getDistrictId())
            .toString();
    }
}
