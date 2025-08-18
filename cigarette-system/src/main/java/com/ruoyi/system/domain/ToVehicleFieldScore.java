package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 车辆各字段风险得分对象 to_vehicle_field_score
 */
@Data
public class ToVehicleFieldScore extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 车牌号 */
    @Excel(name = "车牌号")
    private String plate;

    /** 车型风险得分 */
    @Excel(name = "车型风险得分")
    private Integer vehicleTypeScore;

    /** 品牌风险得分 */
    @Excel(name = "品牌风险得分")
    private Integer brandScore;

    /** 子品牌风险得分 */
    @Excel(name = "子品牌风险得分")
    private Integer subBrandScore;

    /** 车牌风险得分 */
    @Excel(name = "车牌风险得分")
    private Integer plateRiskScore;

    /** 人脸风险得分 */
    @Excel(name = "人脸风险得分")
    private Integer faceScore;

    /** 时间风险得分 */
    @Excel(name = "时间风险得分")
    private Integer timeScore;

    /** 月份风险得分 */
    @Excel(name = "月份风险得分")
    private Integer monthScore;

    /** 地点风险得分 */
    @Excel(name = "地点风险得分")
    private Integer locationScore;

    /** 总风险分 */
    @Excel(name = "总风险分")
    private Double totalScore;

    /** 创建时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date createTime;

    /** 更新时间 */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date updateTime;

    // Getters and Setters
//    public Long getId() { return id; }
//    public void setId(Long id) { this.id = id; }
//
//    public String getPlate() { return plate; }
//    public void setPlate(String plate) { this.plate = plate; }
//
//    public Integer getVehicleTypeScore() { return vehicleTypeScore != null ? vehicleTypeScore : 0; }
//    public void setVehicleTypeScore(Integer vehicleTypeScore) { this.vehicleTypeScore = vehicleTypeScore; }
//
//    public Integer getBrandScore() { return brandScore != null ? brandScore : 0; }
//    public void setBrandScore(Integer brandScore) { this.brandScore = brandScore; }
//
//    public Integer getSubBrandScore() { return subBrandScore != null ? subBrandScore : 0; }
//    public void setSubBrandScore(Integer subBrandScore) { this.subBrandScore = subBrandScore; }
//
//    public Integer getPlateRiskScore() { return plateRiskScore != null ? plateRiskScore : 0; }
//    public void setPlateRiskScore(Integer plateRiskScore) { this.plateRiskScore = plateRiskScore; }
//
//    public Integer getFaceScore() { return faceScore != null ? faceScore : 0; }
//    public void setFaceScore(Integer faceScore) { this.faceScore = faceScore; }
//
//    public Integer getTimeScore() { return timeScore != null ? timeScore : 0; }
//    public void setTimeScore(Integer timeScore) { this.timeScore = timeScore; }
//
//    public Integer getMonthScore() { return monthScore != null ? monthScore : 0; }
//    public void setMonthScore(Integer monthScore) { this.monthScore = monthScore; }
//
//    public Integer getLocationScore() { return locationScore != null ? locationScore : 0; }
//    public void setLocationScore(Integer locationScore) { this.locationScore = locationScore; }
//
//    public Double getTotalScore() { return totalScore != null ? totalScore : 0.0; }
//    public void setTotalScore(Double totalScore) { this.totalScore = totalScore; }
//
//    public Date getCreateTime() { return createTime; }
//    public void setCreateTime(Date createTime) { this.createTime = createTime; }
//
//    public Date getUpdateTime() { return updateTime; }
//    public void setUpdateTime(Date updateTime) { this.updateTime = updateTime; }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("plate", getPlate())
                .append("vehicleTypeScore", getVehicleTypeScore())
                .append("brandScore", getBrandScore())
                .append("subBrandScore", getSubBrandScore())
                .append("plateRiskScore", getPlateRiskScore())
                .append("faceScore", getFaceScore())
                .append("timeScore", getTimeScore())
                .append("monthScore", getMonthScore())
                .append("locationScore", getLocationScore())
                .append("totalScore", getTotalScore())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .toString();
    }
}
