package com.ruoyi.system.domain;

import com.ruoyi.common.core.domain.BaseEntity;
import com.ruoyi.common.core.domain.entity.SysDept;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.TreeEntity;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.List;

/**
 * 地区管理对象 tob_district
 *
 * @author ruoyi
 * @date 2024-07-24
 */
public class TobDistrict extends BaseEntity {
    private static final long serialVersionUID = 1L;


    /**
     * 地区id
     */
    private Long districtId;
    /**
     * 父部门ID
     */
    private Long parentId;
    /**
     * 祖级列表
     */
    private String ancestors;
    /**
     * 地区名称
     */
    @Excel(name = "地区名称")
    private String districtName;
    /**
     * 显示顺序
     */
    private Integer orderNum;
    /**
     * 负责人
     */
    @Excel(name = "负责人")
    private String leader;
    /**
     * 联系电话
     */
    @Excel(name = "联系电话")
    private String phone;
    /**
     * 邮箱
     */
    @Excel(name = "邮箱")
    private String email;
    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;
    /**
     * 删除标记
     */
    @Excel(name = "删除标记")
    private String delFlag;
    /**
     * 父地区名称
     */
    private String parentName;
    /**
     * 子地区
     */
    private List<TobDistrict> children = new ArrayList<TobDistrict>();


    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public List<TobDistrict> getChildren() {
        return children;
    }

    public void setChildren(List<TobDistrict> children) {
        this.children = children;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    public String getAncestors() {
        return ancestors;
    }

    public void setAncestors(String ancestors) {
        this.ancestors = ancestors;
    }

    @NotNull(message = "显示顺序不能为空")
    public Integer getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Integer orderNum) {
        this.orderNum = orderNum;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setLeader(String leader) {
        this.leader = leader;
    }

    public String getLeader() {
        return leader;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setDelFlag(String delFlag) {
        this.delFlag = delFlag;
    }

    public String getDelFlag() {
        return delFlag;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
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
                .append("remark", getRemark())
                .toString();
    }
}
