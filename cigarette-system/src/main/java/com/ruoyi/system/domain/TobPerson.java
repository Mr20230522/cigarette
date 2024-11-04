package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 驾驶人员对象 tob_person
 *
 * @author cigarette
 * @date 2024-09-14
 */
public class TobPerson extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 驾驶人ID */
    private Long suspectId;

    /** 身份证ID */
    @Excel(name = "身份证ID")
    private Long identityCard;

    /** 人脸特征编码 */
    @Excel(name = "人脸特征编码")
    private String faceFeature;

    /** 电话号码 */
    @Excel(name = "电话号码")
    private String phone;

    /** 驾驶证号码 */
    @Excel(name = "驾驶证号码")
    private String suspectLicenseNumber;

    /** 姓名 */
    @Excel(name = "姓名")
    private String name;

    /** 年龄 */
    @Excel(name = "年龄")
    private Long age;

    /** 性别 */
    @Excel(name = "性别")
    private String gender;

    /** 地址 */
    @Excel(name = "地址")
    private String address;

    /** 出现次数 */
    @Excel(name = "出现次数")
    private Long occurrenceNumber;

    /** 违法状态 */
    @Excel(name = "违法状态")
    private String illegalStatus;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 删除标记 */
    private String delFlag;

    /** 涉案数 */
    @Excel(name = "涉案数")
    private Long casesInvolved;

    /** 车员图片 */
    @Excel(name = "车员图片")
    private String picture;

    public void setSuspectId(Long suspectId)
    {
        this.suspectId = suspectId;
    }

    public Long getSuspectId()
    {
        return suspectId;
    }
    public void setIdentityCard(Long identityCard)
    {
        this.identityCard = identityCard;
    }

    public Long getIdentityCard()
    {
        return identityCard;
    }
    public void setFaceFeature(String faceFeature)
    {
        this.faceFeature = faceFeature;
    }

    public String getFaceFeature()
    {
        return faceFeature;
    }
    public void setPhone(String phone)
    {
        this.phone = phone;
    }

    public String getPhone()
    {
        return phone;
    }
    public void setSuspectLicenseNumber(String suspectLicenseNumber)
    {
        this.suspectLicenseNumber = suspectLicenseNumber;
    }

    public String getSuspectLicenseNumber()
    {
        return suspectLicenseNumber;
    }
    public void setName(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }
    public void setAge(Long age)
    {
        this.age = age;
    }

    public Long getAge()
    {
        return age;
    }
    public void setGender(String gender)
    {
        this.gender = gender;
    }

    public String getGender()
    {
        return gender;
    }
    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getAddress()
    {
        return address;
    }
    public void setOccurrenceNumber(Long occurrenceNumber)
    {
        this.occurrenceNumber = occurrenceNumber;
    }

    public Long getOccurrenceNumber()
    {
        return occurrenceNumber;
    }
    public void setIllegalStatus(String illegalStatus)
    {
        this.illegalStatus = illegalStatus;
    }

    public String getIllegalStatus()
    {
        return illegalStatus;
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
    public void setCasesInvolved(Long casesInvolved)
    {
        this.casesInvolved = casesInvolved;
    }

    public Long getCasesInvolved()
    {
        return casesInvolved;
    }
    public void setPicture(String picture)
    {
        this.picture = picture;
    }

    public String getPicture()
    {
        return picture;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("suspectId", getSuspectId())
                .append("identityCard", getIdentityCard())
                .append("faceFeature", getFaceFeature())
                .append("phone", getPhone())
                .append("suspectLicenseNumber", getSuspectLicenseNumber())
                .append("name", getName())
                .append("age", getAge())
                .append("gender", getGender())
                .append("address", getAddress())
                .append("occurrenceNumber", getOccurrenceNumber())
                .append("illegalStatus", getIllegalStatus())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("casesInvolved", getCasesInvolved())
                .append("picture", getPicture())
                .toString();
    }
}
