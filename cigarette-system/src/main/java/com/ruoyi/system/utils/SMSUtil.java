package com.ruoyi.system.utils;

import com.cloopen.rest.sdk.BodyType;
import com.cloopen.rest.sdk.CCPRestSmsSDK;
import com.ruoyi.system.domain.ToSMSModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Random;
import java.util.concurrent.TimeUnit;

@Component
public class SMSUtil {
    @Autowired
    private ToSMSModel smsModel;
    @Autowired
    private RedisTemplate<String, String> redisTemplate;


    public String generateSMS(String phone) {
        String templateId = smsModel.getTemplateId();
        // 1. 校验手机号格式
        if (!phone.matches("^1[3-9]\\d{9}$")) {
            throw new RuntimeException("手机号格式错误！");
        }

        // 2. 生成4位随机验证码
        String randomNum = String.format("%04d", new Random().nextInt(9999));

        // 3. 调用容联云API发送短信
        CCPRestSmsSDK sdk = new CCPRestSmsSDK();
        sdk.init("app.cloopen.com", "8883");
        sdk.setAccount(smsModel.getAccountId(), smsModel.getAuthToken());
        sdk.setAppId(smsModel.getAppId());
        sdk.setBodyType(BodyType.Type_JSON);

        String[] datas = {randomNum, "5"}; // 有效期5分钟
        HashMap<String, Object> result = sdk.sendTemplateSMS(phone, templateId, datas);

        // 4. 发送成功则存入Redis（5分钟过期）
        if ("000000".equals(result.get("statusCode"))) {
            redisTemplate.opsForValue().set(phone, randomNum, 5, TimeUnit.MINUTES);
            return randomNum;
        } else {
            throw new RuntimeException("短信发送失败: " + result.get("statusMsg"));
        }
    }

    public boolean validateSMS(String phone, String sms) {
        String storedSms = redisTemplate.opsForValue().get(phone);
        if (storedSms == null) {
            throw new RuntimeException("验证码已过期或未发送！");
        }
        return storedSms.equals(sms);
    }
}