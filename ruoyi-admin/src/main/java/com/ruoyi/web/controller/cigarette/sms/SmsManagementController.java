package com.ruoyi.web.controller.cigarette.sms;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.utils.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/message")
public class SmsManagementController {
    @Autowired
    private SMSUtil smsUtil;

    @PostMapping("/send")
    public AjaxResult sendSms(@RequestBody Map<String, String> map) {
        String phone = map.get("phone");
        try {
            String code = smsUtil.generateSMS(phone);
            return AjaxResult.success("验证码发送成功", code);
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}
