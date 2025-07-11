package com.ruoyi.system.domain;

import com.ruoyi.common.annotation.Excel;
import com.ruoyi.common.core.domain.BaseEntity;

public class TobAiChatRecord extends BaseEntity {
    private static final long serialVersionUID = 1L;

    private Long id;

    @Excel(name = "会话ID")
    private String sessionId;

    @Excel(name = "用户输入")
    private String userInput;

    @Excel(name = "AI响应")
    private String aiResponse;

    public TobAiChatRecord() {
    }

    public TobAiChatRecord(Long id, String sessionId, String userInput, String aiResponse) {
        this.id = id;
        this.sessionId = sessionId;
        this.userInput = userInput;
        this.aiResponse = aiResponse;
    }

    /**
     * 获取
     * @return id
     */
    public Long getId() {
        return id;
    }

    /**
     * 设置
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * 获取
     * @return sessionId
     */
    public String getSessionId() {
        return sessionId;
    }

    /**
     * 设置
     * @param sessionId
     */
    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    /**
     * 获取
     * @return userInput
     */
    public String getUserInput() {
        return userInput;
    }

    /**
     * 设置
     * @param userInput
     */
    public void setUserInput(String userInput) {
        this.userInput = userInput;
    }

    /**
     * 获取
     * @return aiResponse
     */
    public String getAiResponse() {
        return aiResponse;
    }

    /**
     * 设置
     * @param aiResponse
     */
    public void setAiResponse(String aiResponse) {
        this.aiResponse = aiResponse;
    }

    public String toString() {
        return "AiChatRecord{serialVersionUID = " + serialVersionUID + ", " +
                "id = " + id + ", sessionId = " + sessionId + ", " +
                "userInput = " + userInput + ", " +
                "aiResponse = " + aiResponse + "}";
    }


}