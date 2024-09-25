package com.ruoyi.system.domain;

import java.util.Date;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.builder.ToStringStyle;
import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

/**
 * 问题意见反馈对象 tob_feedback
 *
 * @author ruoyi
 * @date 2024-09-25
 */
public class TobFeedback extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 意见ID */
    private Long id;

    /** 反馈人ID */
    @Excel(name = "反馈人ID")
    private Long creatorId;

    /** 反馈人姓名 */
    @Excel(name = "反馈人姓名")
    private String creatorNickName;

    /** 反馈人账号 */
    @Excel(name = "反馈人账号")
    private String creatorUserName;

    /** 反馈人手机号码 */
    @Excel(name = "反馈人手机号码")
    private String creatorPhoneNumber;

    /** 反馈问题类型 (1: 功能异常, 2: 安全问题, 3: 体验问题, 4: 功能建议, 5: 其它) */
    @Excel(name = "反馈问题类型 (1: 功能异常, 2: 安全问题, 3: 体验问题, 4: 功能建议, 5: 其它)")
    private Long questionType;

    /** 反馈问题/建议详情 */
    @Excel(name = "反馈问题/建议详情")
    private String questionContent;

    /** 反馈问题/建议所在页面 */
    @Excel(name = "反馈问题/建议所在页面")
    private String inPage;

    /** 附件截图保存地址 */
    @Excel(name = "附件截图保存地址")
    private String filePaths;

    /** 问题优先级 (1: LOW, 2: MEDIUM, 3: HIGH) */
    @Excel(name = "问题优先级 (1: LOW, 2: MEDIUM, 3: HIGH)")
    private Long priority;

    /** 状态 (0: 未提交, 1: 已提交, 2: 已删除, 3: 已解决, 4: 不予解决, 5: 解决中) */
    @Excel(name = "状态 (0: 未提交, 1: 已提交, 2: 已删除, 3: 已解决, 4: 不予解决, 5: 解决中)")
    private Long status;

    /** 解决人ID */
    @Excel(name = "解决人ID")
    private Long solverId;

    /** 解决人姓名 */
    @Excel(name = "解决人姓名")
    private String solverNickName;

    /** 解决人账号 */
    @Excel(name = "解决人账号")
    private String solverUserName;

    /** 解决人手机号码 */
    @Excel(name = "解决人手机号码")
    private String solverPhoneNumber;

    /** 解决反馈时间 */
    @JsonFormat(pattern = "yyyy-MM-dd")
    @Excel(name = "解决反馈时间", width = 30, dateFormat = "yyyy-MM-dd")
    private Date solveTime;

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId()
    {
        return id;
    }
    public void setCreatorId(Long creatorId)
    {
        this.creatorId = creatorId;
    }

    public Long getCreatorId()
    {
        return creatorId;
    }
    public void setCreatorNickName(String creatorNickName)
    {
        this.creatorNickName = creatorNickName;
    }

    public String getCreatorNickName()
    {
        return creatorNickName;
    }
    public void setCreatorUserName(String creatorUserName)
    {
        this.creatorUserName = creatorUserName;
    }

    public String getCreatorUserName()
    {
        return creatorUserName;
    }
    public void setCreatorPhoneNumber(String creatorPhoneNumber)
    {
        this.creatorPhoneNumber = creatorPhoneNumber;
    }

    public String getCreatorPhoneNumber()
    {
        return creatorPhoneNumber;
    }
    public void setQuestionType(Long questionType)
    {
        this.questionType = questionType;
    }

    public Long getQuestionType()
    {
        return questionType;
    }
    public void setQuestionContent(String questionContent)
    {
        this.questionContent = questionContent;
    }

    public String getQuestionContent()
    {
        return questionContent;
    }
    public void setInPage(String inPage)
    {
        this.inPage = inPage;
    }

    public String getInPage()
    {
        return inPage;
    }
    public void setFilePaths(String filePaths)
    {
        this.filePaths = filePaths;
    }

    public String getFilePaths()
    {
        return filePaths;
    }
    public void setPriority(Long priority)
    {
        this.priority = priority;
    }

    public Long getPriority()
    {
        return priority;
    }
    public void setStatus(Long status)
    {
        this.status = status;
    }

    public Long getStatus()
    {
        return status;
    }
    public void setSolverId(Long solverId)
    {
        this.solverId = solverId;
    }

    public Long getSolverId()
    {
        return solverId;
    }
    public void setSolverNickName(String solverNickName)
    {
        this.solverNickName = solverNickName;
    }

    public String getSolverNickName()
    {
        return solverNickName;
    }
    public void setSolverUserName(String solverUserName)
    {
        this.solverUserName = solverUserName;
    }

    public String getSolverUserName()
    {
        return solverUserName;
    }
    public void setSolverPhoneNumber(String solverPhoneNumber)
    {
        this.solverPhoneNumber = solverPhoneNumber;
    }

    public String getSolverPhoneNumber()
    {
        return solverPhoneNumber;
    }
    public void setSolveTime(Date solveTime)
    {
        this.solveTime = solveTime;
    }

    public Date getSolveTime()
    {
        return solveTime;
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this,ToStringStyle.MULTI_LINE_STYLE)
                .append("id", getId())
                .append("creatorId", getCreatorId())
                .append("creatorNickName", getCreatorNickName())
                .append("creatorUserName", getCreatorUserName())
                .append("creatorPhoneNumber", getCreatorPhoneNumber())
                .append("createTime", getCreateTime())
                .append("questionType", getQuestionType())
                .append("questionContent", getQuestionContent())
                .append("inPage", getInPage())
                .append("filePaths", getFilePaths())
                .append("priority", getPriority())
                .append("status", getStatus())
                .append("solverId", getSolverId())
                .append("solverNickName", getSolverNickName())
                .append("solverUserName", getSolverUserName())
                .append("solverPhoneNumber", getSolverPhoneNumber())
                .append("solveTime", getSolveTime())
                .toString();
    }
}
