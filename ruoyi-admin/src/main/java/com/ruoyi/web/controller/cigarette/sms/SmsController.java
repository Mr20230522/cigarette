package com.ruoyi.web.controller.cigarette.sms;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.ToSMSModel;
import com.ruoyi.system.utils.SMSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

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
    public AjaxResult verifySms(@RequestParam String phone, @RequestParam String code) {
        try {
            boolean isValid = smsUtil.validateSMS(phone, code);
            return isValid ? AjaxResult.success("验证成功") : AjaxResult.error("验证码错误");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }
    }
}