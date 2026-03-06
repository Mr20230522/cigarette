package com.ruoyi.system.domain.vo;

/**
 * 摄像头ID和名称的VO类
 */
public class CameraIdNameVO {

    /** 摄像头ID */
    private Long cameraId;

    /** 摄像头名称 */
    private String cameraName;

    public CameraIdNameVO() {
    }

    public CameraIdNameVO(Long cameraId, String cameraName) {
        this.cameraId = cameraId;
        this.cameraName = cameraName;
    }

    public Long getCameraId() {
        return cameraId;
    }

    public void setCameraId(Long cameraId) {
        this.cameraId = cameraId;
    }

    public String getCameraName() {
        return cameraName;
    }

    public void setCameraName(String cameraName) {
        this.cameraName = cameraName;
    }

    @Override
    public String toString() {
        return "CameraIdNameVO{" +
                "cameraId=" + cameraId +
                ", cameraName='" + cameraName + '\'' +
                '}';
    }
}