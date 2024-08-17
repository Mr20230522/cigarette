package com.ruoyi.system.domain.vo;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.system.domain.TobCaseInformation;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;

/**
 * 车辆行为记录对象 tob_case_information
 *
 * @author cigarette
 * @date 2024-08-06
 */
public class TobCaseInformationVo extends TobCaseInformation {

    /** 车牌编号 */
    @Excel(name = "车牌")
    private String licensePlate;

    /** 驾驶人员姓名 */
    @Excel(name = "驾驶人姓名")
    private String driverName;

    /** 检查人员姓名 */
    @Excel(name = "检查人员姓名")
    private String staffName;

    /** 案件录入人员姓名 */
    @Excel(name = "案件录入人员姓名")
    private String caseUserName;

    public String getLicensePlate() {
        return licensePlate;
    }

    public void setLicensePlate(String licensePlate) {
        this.licensePlate = licensePlate;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getStaffName() {
        return staffName;
    }

    public void setStaffName(String staffName) {
        this.staffName = staffName;
    }

    public String getCaseUserName() {
        return caseUserName;
    }

    public void setCaseUserName(String caseUserName) {
        this.caseUserName = caseUserName;
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
                .append("behaviorId", getBehaviorId())
                .append("caseSource", getCaseSource())
                .append("caseType", getCaseType())
                .append("caseStatus", getCaseStatus())
                .append("mainCharge", getMainCharge())
                .append("secondaryCharge", getSecondaryCharge())
                .append("mainCaseDetails", getMainCaseDetails())
                .append("entryDate", getEntryDate())
                .append("delFlag", getDelFlag())
                .append("remark", getRemark())
                .append("caseUserId", getCaseUserId())
                .append("licensePlate", getLicensePlate())
                .append("driverName", getDriverName())
                .append("staffName", getStaffName())
                .append("caseUserName", getCaseUserName())
                .toString();
    }
}
