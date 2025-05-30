package com.ruoyi.web.controller.cigarette.sms;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ToSMSModel;
import com.ruoyi.system.utils.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/sms")
public class SmsController {
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

    @PostMapping("/verify")
    @ResponseBody // 确保添加这个注解
    public Map<String, Object> verifySms(@RequestBody Map<String, String> data) {
        Map<String, Object> result = new HashMap<>();
        String phone = data.get("phone");
        String code = data.get("code");

        System.out.println("phone: " + phone + " code: " + code);

        if (phone == null || code == null) {
            result.put("success", false);
            result.put("message", "手机号或验证码不能为空");
            return result;
        }

        try {
            boolean isValid = smsUtil.validateSMS(phone, code);
            System.out.println("验证结果: " + isValid);

            result.put("success", isValid);
            result.put("message", isValid ? "验证成功" : "验证码错误");
            return result;
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", e.getMessage());
            return result;
        }
    }
}