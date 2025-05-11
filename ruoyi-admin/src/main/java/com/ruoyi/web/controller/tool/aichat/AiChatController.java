package com.ruoyi.web.controller.tool.aichat;


import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.TobAiChatRecord;
import com.ruoyi.system.service.ITobAiChatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/ai/chat")
public class AiChatController extends BaseController {

    @Autowired
    private ITobAiChatService aiChatService;

    @PostMapping("/send")
    public AjaxResult sendMessage(@RequestBody Map<String, String> request) {
        String message = request.get("message");
        String sessionId = request.getOrDefault("sessionId",
                "session-" + System.currentTimeMillis() + "-" + UUID.randomUUID().toString().substring(0, 8));

        String response = aiChatService.chatWithAI(message, sessionId);
        System.out.println("AI响应生成成功：" + response);

        // 关键修改：确保返回标准数据结构
        return AjaxResult.success()
                .put("code", 200)
                .put("msg", "操作成功")
                .put("data", new HashMap<String, String>() {{
                    put("response", response);
                    put("sessionId", sessionId);
                }});
    }

    @GetMapping("/history")
    public AjaxResult getChatHistory(@RequestParam String sessionId) {
        TobAiChatRecord record = new TobAiChatRecord();
        record.setSessionId(sessionId);
        List<TobAiChatRecord> list = aiChatService.selectChatRecordList(record);
        return success(list);
    }
}
