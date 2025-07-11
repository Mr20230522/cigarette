package com.ruoyi.system.service.impl;

import com.ruoyi.system.domain.TobAiChatRecord;
import com.ruoyi.system.mapper.TobAiChatRecordMapper;
import com.ruoyi.system.service.ITobAiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.util.List;

@Service
public class TobAiChatServiceImpl implements ITobAiChatService {

    @Autowired
    private TobAiChatRecordMapper aiChatRecordMapper;

    private final RestTemplate restTemplate = new RestTemplate();

    // 配置参数
    private static final int MAX_HISTORY = 3; // 最大记忆轮次
    private static final boolean ENABLE_CONTEXT = true; // 是否启用上下文

    @Override
    public String chatWithAI(String input, String sessionId) {
        try {
            // 1. 获取上下文（如果启用）
            String processedInput = ENABLE_CONTEXT ?
                    buildContextualInput(input, sessionId) :
                    input;

            // 2. 调用AI接口
            String aiResponse = getQingyunkeReply(processedInput);

            // 3. 保存对话记录
            saveDialog(sessionId, input, aiResponse);

            return aiResponse;
        } catch (Exception e) {
            e.printStackTrace();
            return "AI助手暂时无法响应，请稍后再试";
        }
    }

    /**
     * 构建带上下文的输入
     */
    private String buildContextualInput(String currentInput, String sessionId) {
        // 1. 获取历史对话
        List<TobAiChatRecord> history = aiChatRecordMapper.selectRecentBySessionId(
                sessionId,
                MAX_HISTORY
        );

        // 2. 构建上下文提示
        StringBuilder context = new StringBuilder();
        if (!history.isEmpty()) {
            context.append("以下是我们的对话历史：\n");
            for (TobAiChatRecord record : history) {
                context.append("用户：").append(record.getUserInput()).append("\n");
                context.append("助手：").append(record.getAiResponse()).append("\n");
            }
        }

        // 3. 添加当前问题
        context.append("请根据上述对话回答这个问题：").append(currentInput);
        return context.toString();
    }

    /**
     * 调用青云客API（优化版）
     */
    private String getQingyunkeReply(String input) throws Exception {
        String encodedInput = URLEncoder.encode(input, "UTF-8");
        String apiUrl = "http://api.qingyunke.com/api.php?key=free&appid=0&msg=" + encodedInput;

        String response = restTemplate.getForObject(apiUrl, String.class);

        // 更健壮的JSON解析
        String contentKey = "\"content\":\"";
        int start = response.indexOf(contentKey) + contentKey.length();
        int end = response.indexOf("\"", start);

        return response.substring(start, end)
                .replace("{br}", "\n")
                .replace("菲菲", "我");
    }

    /**
     * 保存对话记录
     */
    private void saveDialog(String sessionId, String userInput, String aiResponse) {
        TobAiChatRecord record = new TobAiChatRecord();
        record.setSessionId(sessionId);
        record.setUserInput(userInput);
        record.setAiResponse(aiResponse);
        aiChatRecordMapper.insertAiChatRecord(record);
    }

    @Override
    public List<TobAiChatRecord> selectChatRecordList(TobAiChatRecord record) {
        return aiChatRecordMapper.selectAiChatRecordList(record);
    }
}