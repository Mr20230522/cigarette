package com.ruoyi.system.domain;

import java.math.BigInteger;
import java.time.LocalDateTime;

public class TrafficData {


    private Long Id;

    private Integer cameraId;

    private String cameraName;

    private Integer laneNo;

    private String direction;

    private Integer speed;

    private String plate;

    private String plateType;

    private String plateColor;

    private String vehicleColor;

    private String vehicleType;

    private String vehicleLogo;

    private String vehicleLogoAll;

    private LocalDateTime captureTime;

    private String picUrl;

    private String plateLicense;

    private Integer fileNo;

    private Long startOffset;

    private String srcJson;

    private Double level;

    private String cleanupStatus;

    private LocalDateTime cleanupTime;

    private LocalDateTime createdAt;

    private Integer ctrl;

    private Integer drive;

    private Integer part;

    private String picDownloadStatus;

    private Integer picLen;

    private String plateDownloadStatus;

    private LocalDateTime updatedAt;

    private String videoDownloadStatus;

    private LocalDateTime videoDownloadTime;

    private String videoFilePath;

    // Getters and Setters

    public Long getId() {
        return Id;
    }

    public void setId(Long Id) {
        this.Id = Id;
    }

    public Integer getCameraId() {
        return cameraId;
    }

    public void setCameraId(Integer cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    public Integer getLaneNo() {
        return laneNo;
    }

    public void setLaneNo(Integer laneNo) {
        this.laneNo = laneNo;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public Integer getSpeed() {
        return speed;
    }

    public void setSpeed(Integer speed) {
        this.speed = speed;
    }

    public String getPlate() {
        return plate;
    }

    public void setPlate(String plate) {
        this.plate = plate;
    }

    public String getPlateType() {
        return plateType;
    }

    public void setPlateType(String plateType) {
        this.plateType = plateType;
    }

    public String getPlateColor() {
        return plateColor;
    }

    public void setPlateColor(String plateColor) {
        this.plateColor = plateColor;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getVehicleLogo() {
        return vehicleLogo;
    }

    public void setVehicleLogo(String vehicleLogo) {
        this.vehicleLogo = vehicleLogo;
    }

    public String getVehicleLogoAll() {
        return vehicleLogoAll;
    }

    public void setVehicleLogoAll(String vehicleLogoAll) {
        this.vehicleLogoAll = vehicleLogoAll;
    }

    public LocalDateTime getCaptureTime() {
        return captureTime;
    }

    public void setCaptureTime(LocalDateTime captureTime) {
        this.captureTime = captureTime;
    }

    public String getPicUrl() {
        return picUrl;
    }

    public void setPicUrl(String picUrl) {
        this.picUrl = picUrl;
    }

    public String getPlateLicense() {
        return plateLicense;
    }

    public void setPlateLicense(String plateLicense) {
        this.plateLicense = plateLicense;
    }

    public Integer getFileNo() {
        return fileNo;
    }

    public void setFileNo(Integer fileNo) {
        this.fileNo = fileNo;
    }

    public Long getStartOffset() {
        return startOffset;
    }

    public void setStartOffset(Long startOffset) {
        this.startOffset = startOffset;
    }

    public String getSrcJson() {
        return srcJson;
    }

    public void setSrcJson(String srcJson) {
        this.srcJson = srcJson;
    }

    public Double getLevel() {
        return level;
    }

    public void setLevel(Double level) {
        this.level = level;
    }

    public String getCleanupStatus() {
        return cleanupStatus;
    }

    public void setCleanupStatus(String cleanupStatus) {
        this.cleanupStatus = cleanupStatus;
    }

    public LocalDateTime getCleanupTime() {
        return cleanupTime;
    }

    public void setCleanupTime(LocalDateTime cleanupTime) {
        this.cleanupTime = cleanupTime;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Integer getCtrl() {
        return ctrl;
    }

    public void setCtrl(Integer ctrl) {
        this.ctrl = ctrl;
    }

    public Integer getDrive() {
        return drive;
    }

    public void setDrive(Integer drive) {
        this.drive = drive;
    }

    public Integer getPart() {
        return part;
    }

    public void setPart(Integer part) {
        this.part = part;
    }

    public String getPicDownloadStatus() {
        return picDownloadStatus;
    }

    public void setPicDownloadStatus(String picDownloadStatus) {
        this.picDownloadStatus = picDownloadStatus;
    }

    public Integer getPicLen() {
        return picLen;
    }

    public void setPicLen(Integer picLen) {
        this.picLen = picLen;
    }

    public String getPlateDownloadStatus() {
        return plateDownloadStatus;
    }

    public void setPlateDownloadStatus(String plateDownloadStatus) {
        this.plateDownloadStatus = plateDownloadStatus;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getVideoDownloadStatus() {
        return videoDownloadStatus;
    }

    public void setVideoDownloadStatus(String videoDownloadStatus) {
        this.videoDownloadStatus = videoDownloadStatus;
    }

    public LocalDateTime getVideoDownloadTime() {
        return videoDownloadTime;
    }

    public void setVideoDownloadTime(LocalDateTime videoDownloadTime) {
        this.videoDownloadTime = videoDownloadTime;
    }

    public String getVideoFilePath() {
        return videoFilePath;
    }

    public void setVideoFilePath(String videoFilePath) {
        this.videoFilePath = videoFilePath;
    }

    @Override
    public String toString() {
        return "TrafficData{" +
                "Id=" + Id +
                ", cameraId=" + cameraId +
                ", cameraName='" + cameraName + '\'' +
                ", laneNo=" + laneNo +
                ", direction='" + direction + '\'' +
                ", speed=" + speed +
                ", plate='" + plate + '\'' +
                ", plateType='" + plateType + '\'' +
                ", plateColor='" + plateColor + '\'' +
                ", vehicleColor='" + vehicleColor + '\'' +
                ", vehicleType='" + vehicleType + '\'' +
                ", vehicleLogo='" + vehicleLogo + '\'' +
                ", vehicleLogoAll='" + vehicleLogoAll + '\'' +
                ", captureTime=" + captureTime +
                ", picUrl='" + picUrl + '\'' +
                ", plateLicense='" + plateLicense + '\'' +
                ", fileNo=" + fileNo +
                ", startOffset=" + startOffset +
                ", srcJson='" + srcJson + '\'' +
                ", level=" + level +
                ", cleanupStatus='" + cleanupStatus + '\'' +
                ", cleanupTime=" + cleanupTime +
                ", createdAt=" + createdAt +
                ", ctrl=" + ctrl +
                ", drive=" + drive +
                ", part=" + part +
                ", picDownloadStatus='" + picDownloadStatus + '\'' +
                ", picLen=" + picLen +
                ", plateDownloadStatus='" + plateDownloadStatus + '\'' +
                ", updatedAt=" + updatedAt +
                ", videoDownloadStatus='" + videoDownloadStatus + '\'' +
                ", videoDownloadTime=" + videoDownloadTime +
                ", videoFilePath='" + videoFilePath + '\'' +
                '}';
    }
}
