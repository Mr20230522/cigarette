package com.ruoyi.system.domain;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 文档对象 tob_document
 * 
 * @author cigarette
 * @date 2024-07-27
 */
public class TobDocument extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 原文件名（上传前文件名） */
    @Excel(name = "原文件名", readConverterExp = "上=传前文件名")
    private String fileName;

    /** 文件后缀（.xls;.xlsx;.doc;.;.） */
    @Excel(name = "文件后缀", readConverterExp = ".=xls;.xlsx;.doc;.;.")
    private String fileType;

    /** 上传后文件路径 */
    @Excel(name = "上传后文件路径")
    private String uploadPath;

    /** 上传后文件名 */
    @Excel(name = "上传后文件名")
    private String uploadFileName;

    /** 转换pdf路径 */
    @Excel(name = "转换pdf路径")
    private String pdfPath;

    /** 转换pdf文件名 */
    @Excel(name = "转换pdf文件名")
    private String pdfName;

    /** 文档类型 */
    @Excel(name = "文档类型")
    private String documentType;

    /** 预览URL */
    @Excel(name = "预览URL")
    private String previewUrl;

    /**
     * 监测区域id
     */
    @Excel(name = "监测区域id")
    private Long detectionId;

    /**
     * 地区id
     */
    @Excel(name = "地区id")
    private Long districtId;

    public void setId(Long id) 
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setFileType(String fileType) 
    {
        this.fileType = fileType;
    }

    public String getFileType() 
    {
        return fileType;
    }
    public void setUploadPath(String uploadPath) 
    {
        this.uploadPath = uploadPath;
    }

    public String getUploadPath() 
    {
        return uploadPath;
    }
    public void setUploadFileName(String uploadFileName) 
    {
        this.uploadFileName = uploadFileName;
    }

    public String getUploadFileName() 
    {
        return uploadFileName;
    }
    public void setPdfPath(String pdfPath) 
    {
        this.pdfPath = pdfPath;
    }

    public String getPdfPath() 
    {
        return pdfPath;
    }
    public void setPdfName(String pdfName) 
    {
        this.pdfName = pdfName;
    }

    public String getPdfName() 
    {
        return pdfName;
    }
    public void setDocumentType(String documentType) 
    {
        this.documentType = documentType;
    }

    public String getDocumentType() 
    {
        return documentType;
    }
    public void setPreviewUrl(String previewUrl) 
    {
        this.previewUrl = previewUrl;
    }

    public String getPreviewUrl() 
    {
        return previewUrl;
    }

    public Long getDetectionId() {
        return detectionId;
    }

    public void setDetectionId(Long detectionId) {
        this.detectionId = detectionId;
    }

    public Long getDistrictId() {
        return districtId;
    }

    public void setDistrictId(Long districtId) {
        this.districtId = districtId;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
            .append("id", getId())
            .append("fileName", getFileName())
            .append("fileType", getFileType())
            .append("uploadPath", getUploadPath())
            .append("uploadFileName", getUploadFileName())
            .append("pdfPath", getPdfPath())
            .append("pdfName", getPdfName())
            .append("documentType", getDocumentType())
            .append("createTime", getCreateTime())
            .append("createBy", getCreateBy())
            .append("updateTime", getUpdateTime())
            .append("updateBy", getUpdateBy())
            .append("remark", getRemark())
            .append("previewUrl", getPreviewUrl())
                .append("detectionId", getDetectionId())
                .append("districtId", getDistrictId())
            .toString();
    }
}
