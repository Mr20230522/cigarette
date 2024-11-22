package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 片段化视频对象 tob_time_video
 * 
 * @author muyouzhi
 * @date 2024-10-27
 */
public class TobTimeVideo extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 视频ID */
    private Long videoId;

    /** 视频url */
    @Excel(name = "视频url")
    private String videoPath;

    /** 视频名称 */
    @Excel(name = "视频名称")
    private String videoName;

    /** 摄像头id */
    @Excel(name = "摄像头id")
    private Long cameraId;

    /** 视频大小 */
    @Excel(name = "视频大小")
    private Long videoSize;

    /** 监测区域id */
    @Excel(name = "监测区域id")
    private Long detectionId;

    /** 地区id */
    @Excel(name = "地区id")
    private Long districtId;

    /** 状态 */
    @Excel(name = "状态")
    private String status;

    public void setVideoId(Long videoId) 
    {
        this.videoId = videoId;
    }

    public Long getVideoId() 
    {
        return videoId;
    }
    public void setVideoPath(String videoPath) 
    {
        this.videoPath = videoPath;
    }

    public String getVideoPath() 
    {
        return videoPath;
    }
    public void setVideoName(String videoName) 
    {
        this.videoName = videoName;
    }

    public String getVideoName() 
    {
        return videoName;
    }
    public void setCameraId(Long cameraId) 
    {
        this.cameraId = cameraId;
    }

    public Long getCameraId() 
    {
        return cameraId;
    }
    public void setVideoSize(Long videoSize) 
    {
        this.videoSize = videoSize;
    }

    public Long getVideoSize() 
    {
        return videoSize;
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
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("videoId", getVideoId())
            .append("videoPath", getVideoPath())
            .append("videoName", getVideoName())
            .append("cameraId", getCameraId())
            .append("videoSize", getVideoSize())
            .append("detectionId", getDetectionId())
            .append("districtId", getDistrictId())
            .append("remark", getRemark())
            .append("status", getStatus())
            .append("createTime", getCreateTime())
            .toString();
    }
}
