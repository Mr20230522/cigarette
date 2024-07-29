package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 案件信息对象 tob_case_information
 * 
 * @author cigarette
 * @date 2024-07-28
 */
public class TobCaseInformation extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 案件ID */
    private Long caseId;

    /** 案件代码 */
    @Excel(name = "案件代码")
    private String caseCode;

    /** 案件名称 */
    @Excel(name = "案件名称")
    private String caseName;

    /** 立案时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "立案时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date caseStartDate;

    /** 结案时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "结案时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date caseEndDate;

    /** 案值 */
    @Excel(name = "案值")
    private BigDecimal caseValue;

    /** 案发地点 */
    @Excel(name = "案发地点")
    private String incidentLocation;

    /** 违法人员id */
    @Excel(name = "违法人员id")
    private Long illegalId;

    /** 违法车辆id */
    @Excel(name = "违法车辆id")
    private Long behaviorId;

    /** 案件来源ID */
    @Excel(name = "案件来源ID")
    private String caseSourceId;

    /** 案件类型 */
    @Excel(name = "案件类型")
    private String caseType;

    /** 案件状态 */
    @Excel(name = "案件状态")
    private String caseStatus;

    /** 主要案由 */
    @Excel(name = "主要案由")
    private String mainCharge;

    /** 次要案由 */
    @Excel(name = "次要案由")
    private String secondaryCharge;

    /** 案情描述 */
    @Excel(name = "案情描述")
    private String mainCaseDetails;

    /** 录入日期 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "录入日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date entryDate;

    /** 删除标记 */
    private String delFlag;

    /** 案件人id */
    @Excel(name = "案件人id")
    private Long caseUserId;

    public void setCaseId(Long caseId) 
    {
        this.caseId = caseId;
    }

    public Long getCaseId() 
    {
        return caseId;
    }
    public void setCaseCode(String caseCode) 
    {
        this.caseCode = caseCode;
    }

    public String getCaseCode() 
    {
        return caseCode;
    }
    public void setCaseName(String caseName) 
    {
        this.caseName = caseName;
    }

    public String getCaseName() 
    {
        return caseName;
    }
    public void setCaseStartDate(Date caseStartDate) 
    {
        this.caseStartDate = caseStartDate;
    }

    public Date getCaseStartDate() 
    {
        return caseStartDate;
    }
    public void setCaseEndDate(Date caseEndDate) 
    {
        this.caseEndDate = caseEndDate;
    }

    public Date getCaseEndDate() 
    {
        return caseEndDate;
    }
    public void setCaseValue(BigDecimal caseValue) 
    {
        this.caseValue = caseValue;
    }

    public BigDecimal getCaseValue() 
    {
        return caseValue;
    }
    public void setIncidentLocation(String incidentLocation) 
    {
        this.incidentLocation = incidentLocation;
    }

    public String getIncidentLocation() 
    {
        return incidentLocation;
    }
    public void setIllegalId(Long illegalId) 
    {
        this.illegalId = illegalId;
    }

    public Long getIllegalId() 
    {
        return illegalId;
    }
    public void setBehaviorId(Long behaviorId) 
    {
        this.behaviorId = behaviorId;
    }

    public Long getBehaviorId() 
    {
        return behaviorId;
    }
    public void setCaseSourceId(String caseSourceId) 
    {
        this.caseSourceId = caseSourceId;
    }

    public String getCaseSourceId() 
    {
        return caseSourceId;
    }
    public void setCaseType(String caseType) 
    {
        this.caseType = caseType;
    }

    public String getCaseType() 
    {
        return caseType;
    }
    public void setCaseStatus(String caseStatus) 
    {
        this.caseStatus = caseStatus;
    }

    public String getCaseStatus() 
    {
        return caseStatus;
    }
    public void setMainCharge(String mainCharge) 
    {
        this.mainCharge = mainCharge;
    }

    public String getMainCharge() 
    {
        return mainCharge;
    }
    public void setSecondaryCharge(String secondaryCharge) 
    {
        this.secondaryCharge = secondaryCharge;
    }

    public String getSecondaryCharge() 
    {
        return secondaryCharge;
    }
    public void setMainCaseDetails(String mainCaseDetails) 
    {
        this.mainCaseDetails = mainCaseDetails;
    }

    public String getMainCaseDetails() 
    {
        return mainCaseDetails;
    }
    public void setEntryDate(Date entryDate) 
    {
        this.entryDate = entryDate;
    }

    public Date getEntryDate() 
    {
        return entryDate;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }
    public void setCaseUserId(Long caseUserId) 
    {
        this.caseUserId = caseUserId;
    }

    public Long getCaseUserId() 
    {
        return caseUserId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("caseId", getCaseId())
            .append("caseCode", getCaseCode())
            .append("caseName", getCaseName())
            .append("caseStartDate", getCaseStartDate())
            .append("caseEndDate", getCaseEndDate())
            .append("caseValue", getCaseValue())
            .append("incidentLocation", getIncidentLocation())
            .append("illegalId", getIllegalId())
            .append("behaviorId", getBehaviorId())
            .append("caseSourceId", getCaseSourceId())
            .append("caseType", getCaseType())
            .append("caseStatus", getCaseStatus())
            .append("mainCharge", getMainCharge())
            .append("secondaryCharge", getSecondaryCharge())
            .append("mainCaseDetails", getMainCaseDetails())
            .append("entryDate", getEntryDate())
            .append("delFlag", getDelFlag())
            .append("remark", getRemark())
            .append("caseUserId", getCaseUserId())
            .toString();
    }
}
