package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobAiChatRecord;

import java.util.List;

public interface ITobAiChatService {
    String chatWithAI(String input, String sessionId);
    List<TobAiChatRecord> selectChatRecordList(TobAiChatRecord record);
}