package com.ruoyi.system.domain;

import java.math.BigDecimal;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 摄像头对象 tob_camera
 *
 * @author ruoyi
 * @date 2024-08-02
 */
public class TobCamera extends BaseEntity {
    private static final long serialVersionUID = 1L;

    /**
     * 摄像头id
     */
    private Long cameraId;

    /**
     * 摄像头ip
     */
    @Excel(name = "摄像头ip")
    private String cameraIp;

    /**
     * 摄像头型号
     */
    @Excel(name = "摄像头型号")
    private String cameraModel;

    /**
     * 摄像头厂商
     */
    @Excel(name = "摄像头厂商")
    private String cameraManufacturer;

    /**
     * 监测区域id
     */
    @Excel(name = "监测区域id")
    private Long detectionId;

    /**
     * 分辨率
     */
    @Excel(name = "分辨率")
    private String resolutionRatio;

    /**
     * 帧率
     */
    @Excel(name = "帧率")
    private Long frameRate;

    /**
     * 夜视能力
     */
    @Excel(name = "夜视能力")
    private String nightVision;

    /**
     * 连接方式
     */
    @Excel(name = "连接方式")
    private String connectionType;

    /**
     * 安装日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "安装日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date installationDate;

    /**
     * 质保截止日期
     */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "质保截止日期", width = 30, dateFormat = "yyyy-MM-dd")
    private Date guaranteePeriod;

    /**
     * 经度
     */
    @Excel(name = "经度")
    private BigDecimal longitude;

    /**
     * 纬度
     */
    @Excel(name = "纬度")
    private BigDecimal latitude;

    /**
     * 摄像头类型
     */
    @Excel(name = "摄像头类型")
    private String cameraType;

    /**
     * 摄像头组号
     */
    @Excel(name = "摄像头组号")
    private Long cameraGroupIndication;

    /**
     * 摄像头应用方向
     */
    @Excel(name = "摄像头应用方向")
    private String cameraApplicationType;

    /**
     * 状态
     */
    @Excel(name = "状态")
    private String status;

    /**
     * 删除标记
     */
    private String delFlag;



    /**
     * 地区id
     */
    @Excel(name = "地区id")
    private Long districtId;

    @Excel(name = "摄像头地址")
    private String cameraUrl;


    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraIp(String cameraIp) {
        this.cameraIp = cameraIp;
    }

    public String getCameraIp() {
        return cameraIp;
    }

    public void setCameraModel(String cameraModel) {
        this.cameraModel = cameraModel;
    }

    public String getCameraModel() {
        return cameraModel;
    }

    public void setCameraManufacturer(String cameraManufacturer) {
        this.cameraManufacturer = cameraManufacturer;
    }

    public String getCameraManufacturer() {
        return cameraManufacturer;
    }

    public void setDetectionId(Long detectionId) {
        this.detectionId = detectionId;
    }

    public Long getDetectionId() {
        return detectionId;
    }

    public void setResolutionRatio(String resolutionRatio) {
        this.resolutionRatio = resolutionRatio;
    }

    public String getResolutionRatio() {
        return resolutionRatio;
    }

    public void setFrameRate(Long frameRate) {
        this.frameRate = frameRate;
    }

    public Long getFrameRate() {
        return frameRate;
    }

    public void setNightVision(String nightVision) {
        this.nightVision = nightVision;
    }

    public String getNightVision() {
        return nightVision;
    }

    public void setConnectionType(String connectionType) {
        this.connectionType = connectionType;
    }

    public String getConnectionType() {
        return connectionType;
    }

    public void setInstallationDate(Date installationDate) {
        this.installationDate = installationDate;
    }

    public Date getInstallationDate() {
        return installationDate;
    }

    public void setGuaranteePeriod(Date guaranteePeriod) {
        this.guaranteePeriod = guaranteePeriod;
    }

    public Date getGuaranteePeriod() {
        return guaranteePeriod;
    }

    public void setLongitude(BigDecimal longitude) {
        this.longitude = longitude;
    }

    public BigDecimal getLongitude() {
        return longitude;
    }

    public void setLatitude(BigDecimal latitude) {
        this.latitude = latitude;
    }

    public BigDecimal getLatitude() {
        return latitude;
    }

    public void setCameraType(String cameraType) {
        this.cameraType = cameraType;
    }

    public String getCameraType() {
        return cameraType;
    }

    public void setCameraGroupIndication(Long cameraGroupIndication) {
        this.cameraGroupIndication = cameraGroupIndication;
    }

    public Long getCameraGroupIndication() {
        return cameraGroupIndication;
    }

    public void setCameraApplicationType(String cameraApplicationType) {
        this.cameraApplicationType = cameraApplicationType;
    }

    public String getCameraApplicationType() {
        return cameraApplicationType;
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

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public String getCameraUrl() {
        return cameraUrl;
    }

    public void setCameraUrl(String cameraUrl) {
        this.cameraUrl = cameraUrl;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this, ToStringStyle.MULTI_LINE_STYLE)
                .append("cameraId", getCameraId())
                .append("cameraIp", getCameraIp())
                .append("cameraModel", getCameraModel())
                .append("cameraManufacturer", getCameraManufacturer())
                .append("detectionId", getDetectionId())
                .append("resolutionRatio", getResolutionRatio())
                .append("frameRate", getFrameRate())
                .append("nightVision", getNightVision())
                .append("connectionType", getConnectionType())
                .append("installationDate", getInstallationDate())
                .append("guaranteePeriod", getGuaranteePeriod())
                .append("longitude", getLongitude())
                .append("latitude", getLatitude())
                .append("cameraType", getCameraType())
                .append("cameraGroupIndication", getCameraGroupIndication())
                .append("cameraApplicationType", getCameraApplicationType())
                .append("status", getStatus())
                .append("delFlag", getDelFlag())
                .append("remark", getRemark())
                .append("createTime", getCreateTime())
                .append("updateTime", getUpdateTime())
                .append("districtId", getDistrictId())
                .append("cameraUrl", getCameraUrl())
                .toString();
    }
}
