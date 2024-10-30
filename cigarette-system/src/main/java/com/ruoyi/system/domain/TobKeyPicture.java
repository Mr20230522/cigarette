package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 关键帧图片对象 tob_key_picture
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
public class TobKeyPicture extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 图片id */
    private Long keyPictureId;

    /** 图片url */
    @Excel(name = "图片url")
    private String image;

    /** 图片名称 */
    @Excel(name = "图片名称")
    private String imageName;

    /** 摄像头id */
    @Excel(name = "摄像头id")
    private Long cameraId;

    /** 坐标（标注） */
    @Excel(name = "坐标", readConverterExp = "标=注")
    private String coordinate;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    /** 监测区域id */
    @Excel(name = "监测区域id")
    private Long detectionId;

    /** 地区id */
    @Excel(name = "地区id")
    private Long districtId;

    public void setKeyPictureId(Long keyPictureId) 
    {
        this.keyPictureId = keyPictureId;
    }

    public Long getKeyPictureId() 
    {
        return keyPictureId;
    }
    public void setImage(String image) 
    {
        this.image = image;
    }

    public String getImage() 
    {
        return image;
    }
    public void setImageName(String imageName) 
    {
        this.imageName = imageName;
    }

    public String getImageName() 
    {
        return imageName;
    }
    public void setCameraId(Long cameraId) 
    {
        this.cameraId = cameraId;
    }

    public Long getCameraId() 
    {
        return cameraId;
    }
    public void setCoordinate(String coordinate) 
    {
        this.coordinate = coordinate;
    }

    public String getCoordinate() 
    {
        return coordinate;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDetectionId(Long detectionId) 
    {
        this.detectionId = detectionId;
    }

    public Long getDetectionId() 
    {
        return detectionId;
    }
    public void setDistrictId(Long districtId) 
    {
        this.districtId = districtId;
    }

    public Long getDistrictId() 
    {
        return districtId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("keyPictureId", getKeyPictureId())
            .append("image", getImage())
            .append("createTime", getCreateTime())
            .append("imageName", getImageName())
            .append("cameraId", getCameraId())
            .append("coordinate", getCoordinate())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("detectionId", getDetectionId())
            .append("districtId", getDistrictId())
            .toString();
    }
}
