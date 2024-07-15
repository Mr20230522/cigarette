package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 地区对象 tob_district
 * 
 * @author ruoyi
 * @date 2024-07-11
 */
public class TobDistrict extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 地区id */
    private Long districtId;

    /** 父地区id */
    @Excel(name = "父地区id")
    private Long parentId;

    /** 祖级列表 */
    @Excel(name = "祖级列表")
    private String ancestors;

    /** 地区名称 */
    @Excel(name = "地区名称")
    private String districtName;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 负责人 */
    @Excel(name = "负责人")
    private String leader;

    /** 联系电话 */
    @Excel(name = "联系电话")
    private String phone;

    /** 邮箱 */
    @Excel(name = "邮箱")
    private String email;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;

    public void setDistrictId(Long districtId) 
    {
        this.districtId = districtId;
    }

    public Long getDistrictId() 
    {
        return districtId;
    }
    public void setParentId(Long parentId) 
    {
        this.parentId = parentId;
    }

    public Long getParentId() 
    {
        return parentId;
    }
    public void setAncestors(String ancestors) 
    {
        this.ancestors = ancestors;
    }

    public String getAncestors() 
    {
        return ancestors;
    }
    public void setDistrictName(String districtName) 
    {
        this.districtName = districtName;
    }

    public String getDistrictName() 
    {
        return districtName;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setLeader(String leader) 
    {
        this.leader = leader;
    }

    public String getLeader() 
    {
        return leader;
    }
    public void setPhone(String phone) 
    {
        this.phone = phone;
    }

    public String getPhone() 
    {
        return phone;
    }
    public void setEmail(String email) 
    {
        this.email = email;
    }

    public String getEmail() 
    {
        return email;
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
            .append("districtId", getDistrictId())
            .append("parentId", getParentId())
            .append("ancestors", getAncestors())
            .append("districtName", getDistrictName())
            .append("orderNum", getOrderNum())
            .append("leader", getLeader())
            .append("phone", getPhone())
            .append("email", getEmail())
            .append("status", getStatus())
            .append("delFlag", getDelFlag())
            .append("createTime", getCreateTime())
            .append("updateTime", getUpdateTime())
            .toString();
    }
}
