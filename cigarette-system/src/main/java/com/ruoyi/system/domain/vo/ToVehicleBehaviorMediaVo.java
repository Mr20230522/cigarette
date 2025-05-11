package com.ruoyi.system.domain.vo;

public class ToVehicleBehaviorMediaVo {
    private Long behaviorId;
    private String licensePlatePhotoPath;
    private String vehiclePhotoPath;
    private String vehicleBehaviorVedioPath;

    public ToVehicleBehaviorMediaVo() {
    }

    public ToVehicleBehaviorMediaVo(Long behaviorId, String licensePlatePhotoPath, String vehiclePhotoPath, String vehicleBehaviorVedioPath) {
        this.behaviorId = behaviorId;
        this.licensePlatePhotoPath = licensePlatePhotoPath;
        this.vehiclePhotoPath = vehiclePhotoPath;
        this.vehicleBehaviorVedioPath = vehicleBehaviorVedioPath;
    }

    /**
     * 获取
     * @return behaviorId
     */
    public Long getBehaviorId() {
        return behaviorId;
    }

    /**
     * 设置
     * @param behaviorId
     */
    public void setBehaviorId(Long behaviorId) {
        this.behaviorId = behaviorId;
    }

    /**
     * 获取
     * @return licnesePlatePhotoPath
     */
    public String getLicnesePlatePhotoPath() {
        return licensePlatePhotoPath;
    }

    /**
     * 设置
     * @param licensePlatePhotoPath
     */
    public void setLicnesePlatePhotoPath(String licensePlatePhotoPath) {
        this.licensePlatePhotoPath = licensePlatePhotoPath;
    }

    /**
     * 获取
     * @return vehiclePhotoPath
     */
    public String getVehiclePhotoPath() {
        return vehiclePhotoPath;
    }

    /**
     * 设置
     * @param vehiclePhotoPath
     */
    public void setVehiclePhotoPath(String vehiclePhotoPath) {
        this.vehiclePhotoPath = vehiclePhotoPath;
    }

    /**
     * 获取
     * @return vehicleBehaviorVedioPath
     */
    public String getVehicleBehaviorVedioPath() {
        return vehicleBehaviorVedioPath;
    }

    /**
     * 设置
     * @param vehicleBehaviorVedioPath
     */
    public void setVehicleBehaviorVedioPath(String vehicleBehaviorVedioPath) {
        this.vehicleBehaviorVedioPath = vehicleBehaviorVedioPath;
    }

    public String toString() {
        return "ToVehicleBehaviorVedioVo{behaviorId = " + behaviorId + ", licensePlatePhotoPath = " + licensePlatePhotoPath + ", vehiclePhotoPath = " + vehiclePhotoPath + ", vehicleBehaviorVedioPath = " + vehicleBehaviorVedioPath + "}";
    }
}
