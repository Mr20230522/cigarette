package com.ruoyi.system.service.impl;

import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.ruoyi.system.domain.TobAlertTask;
import com.ruoyi.system.service.IWxworkPushService;
import com.ruoyi.system.service.ITobWxworkConfigService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.net.URLEncoder;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * 企业微信推送服务实现
 * 负责获取AccessToken、推送TextCard消息、生成签名链接
 */
@Service
public class WxworkPushServiceImpl implements IWxworkPushService {

    private static final Logger log = LoggerFactory.getLogger(WxworkPushServiceImpl.class);

    /** Redis缓存Key前缀 */
    private static final String ACCESS_TOKEN_KEY = "wxwork:access_token";

    /** 签名盐值 */
    private static final String SIGN_SALT = "AlertTask2026";

    /** AccessToken缓存时间（7000秒，官方7200秒） */
    private static final long TOKEN_EXPIRE_SECONDS = 7000;

    @Autowired
    private ITobWxworkConfigService tobWxworkConfigService;

    @Autowired(required = false)
    private StringRedisTemplate stringRedisTemplate;

    /** 系统域名，用于生成处理链接 */
    @Value("${wxwork.domain:http://localhost:端口}")
    private String wxworkDomain;

    /** 应用主页地址（企微自建应用的应用主页URL，支持内网IP） */
    @Value("${wxwork.app-home-url:http://192.168.1.100:8080/wxwork/handle.html}")
    private String appHomeUrl;

    @Override
    public String getAccessToken() {
        // 先从Redis获取
        if (stringRedisTemplate != null) {
            String cachedToken = stringRedisTemplate.opsForValue().get(ACCESS_TOKEN_KEY);
            if (cachedToken != null && !cachedToken.isEmpty()) {
                return cachedToken;
            }
        }

        // 获取配置（DB优先，默认值兜底）
        String corpId = tobWxworkConfigService.getConfigValue("wxwork.corpid",
                "");
        String secret = tobWxworkConfigService.getConfigValue("wxwork.secret",
                "");
        // 从application.yml获取默认值兜底
        if (corpId.isEmpty()) {
            corpId = "wx_default_corpid"; // 接入时需要替换
        }
        if (secret.isEmpty()) {
            secret = "default_secret"; // 接入时需要替换
        }

        // 调用企微API获取token
        String url = "https://qyapi.weixin.qq.com/cgi-bin/gettoken?corpid=" + corpId + "&corpsecret=" + secret;
        try {
            HttpResponse response = HttpRequest.get(url).execute();
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                if (result.getInt("errcode") == 0) {
                    String accessToken = result.getStr("access_token");
                    // 缓存到Redis
                    if (stringRedisTemplate != null) {
                        stringRedisTemplate.opsForValue().set(ACCESS_TOKEN_KEY, accessToken, TOKEN_EXPIRE_SECONDS, TimeUnit.SECONDS);
                    }
                    log.info("获取企业微信access_token成功");
                    return accessToken;
                } else {
                    log.error("获取企业微信access_token失败: {}", result.getStr("errmsg"));
                }
            }
        } catch (Exception e) {
            log.error("获取企业微信access_token异常", e);
        }

        return null;
    }

    @Override
    public boolean pushTextCard(TobAlertTask task, String toUser) {
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            log.error("推送TextCard失败：无法获取access_token，taskId={}", task.getId());
            return false;
        }

        String agentId = tobWxworkConfigService.getConfigValue("wxwork.agentid", "1000001");
        String signedUrl = generateSignedUrl(task.getId());

        String title = "【预警通知】" + (task.getPlate() != null ? task.getPlate() : "新任务");
        String description = "车牌号：" + (task.getPlate() != null ? task.getPlate() : "-") + "\n"
                + "预警原因：" + (task.getReason() != null ? task.getReason() : "-") + "\n"
                + "预警等级：" + getLevelFromReason(task.getReason()) + "\n"
                + "卡口：" + (task.getCameraName() != null ? task.getCameraName() : "-") + "\n"
                + "时间：" + (task.getCaptureTime() != null ? DateUtil.format(task.getCaptureTime(), "yyyy-MM-dd HH:mm") : "-") + "\n"
                + "任务处理状态：" + getStatusText(task.getStatus());

        JSONObject body = new JSONObject();
        body.set("touser", toUser);
        body.set("msgtype", "textcard");
        JSONObject textcard = new JSONObject();
        textcard.set("title", title);
        textcard.set("description", description);
        textcard.set("url", signedUrl);
        textcard.set("btntxt", "立即处理");
        body.set("textcard", textcard);
        body.set("agentid", Integer.parseInt(agentId));

        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        try {
            HttpResponse response = HttpRequest.post(url).body(body.toString()).execute();
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                if (result.getInt("errcode") == 0) {
                    log.info("TextCard推送成功，taskId={}, toUser={}", task.getId(), toUser);
                    return true;
                } else {
                    log.error("TextCard推送失败，taskId={}, toUser={}, errcode={}, errmsg={}",
                            task.getId(), toUser, result.getInt("errcode"), result.getStr("errmsg"));
                }
            }
        } catch (Exception e) {
            log.error("TextCard推送异常，taskId={}, toUser={}", task.getId(), toUser, e);
        }
        return false;
    }

    @Override
    public String generateSignedUrl(Long taskId) {
        String corpId = tobWxworkConfigService.getConfigValue("wxwork.corpid", "");
        if (corpId == null || corpId.isEmpty()) {
            log.error("generateSignedUrl失败：wxwork.corpid 未配置");
            return appHomeUrl + "?taskId=" + taskId;
        }

        String encodedRedirect;
        try {
            encodedRedirect = URLEncoder.encode(appHomeUrl, "UTF-8");
        } catch (Exception e) {
            log.error("URL编码失败: {}", e.getMessage());
            return appHomeUrl + "?taskId=" + taskId;
        }

        return "https://open.weixin.qq.com/connect/oauth2/authorize"
                + "?appid=" + corpId
                + "&redirect_uri=" + encodedRedirect
                + "&response_type=code"
                + "&scope=snsapi_base"
                + "&state=taskId:" + taskId
                + "#wechat_redirect";
    }

    /**
     * 群机器人Webhook通知（旁路，不影响主流程）
     * @param webhookUrl 群机器人Webhook地址
     * @param content    Markdown格式消息内容
     */
    public void notifyGroup(String webhookUrl, String content) {
        if (webhookUrl == null || webhookUrl.isEmpty()) {
            return;
        }
        try {
            JSONObject body = new JSONObject();
            body.set("msgtype", "markdown");
            JSONObject markdown = new JSONObject();
            markdown.set("content", content);
            body.set("markdown", markdown);

            HttpResponse response = HttpRequest.post(webhookUrl)
                    .body(body.toString())
                    .execute();
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                if (result.getInt("errcode") == 0) {
                    log.info("群通知发送成功");
                } else {
                    log.warn("群通知发送失败: {}", result.getStr("errmsg"));
                }
            }
        } catch (Exception e) {
            log.warn("群通知发送异常: {}", e.getMessage());
        }
    }

    @Override
    public boolean pushText(TobAlertTask task, String toUser) {
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            log.error("推送Text失败：无法获取access_token，taskId={}", task.getId());
            return false;
        }

        String agentId = tobWxworkConfigService.getConfigValue("wxwork.agentid", "1000001");

        String content = "车牌号：" + (task.getPlate() != null ? task.getPlate() : "-") + "\n"
                + "预警原因：" + (task.getReason() != null ? task.getReason() : "-") + "\n"
                + "预警等级：" + getLevelFromReason(task.getReason()) + "\n"
                + "卡口：" + (task.getCameraName() != null ? task.getCameraName() : "-") + "\n"
                + "时间：" + (task.getCaptureTime() != null ? DateUtil.format(task.getCaptureTime(), "yyyy-MM-dd HH:mm") : "-") + "\n"
                + "任务处理状态：" + getStatusText(task.getStatus());

        JSONObject body = new JSONObject();
        body.set("touser", toUser);
        body.set("msgtype", "text");
        JSONObject text = new JSONObject();
        text.set("content", content);
        body.set("text", text);
        body.set("agentid", Integer.parseInt(agentId));

        String url = "https://qyapi.weixin.qq.com/cgi-bin/message/send?access_token=" + accessToken;
        try {
            HttpResponse response = HttpRequest.post(url).body(body.toString()).execute();
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                if (result.getInt("errcode") == 0) {
                    log.info("Text推送成功，taskId={}, toUser={}", task.getId(), toUser);
                    return true;
                } else {
                    log.error("Text推送失败，taskId={}, toUser={}, errcode={}, errmsg={}",
                            task.getId(), toUser, result.getInt("errcode"), result.getStr("errmsg"));
                }
            }
        } catch (Exception e) {
            log.error("Text推送异常，taskId={}, toUser={}", task.getId(), toUser, e);
        }
        return false;
    }

    @Override
    public String getUserInfoByCode(String code) {
        String accessToken = getAccessToken();
        if (accessToken == null || accessToken.isEmpty()) {
            log.error("getUserInfoByCode失败：无法获取access_token");
            return null;
        }
        String url = "https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token="
                + accessToken + "&code=" + code;
        try {
            HttpResponse response = HttpRequest.get(url).execute();
            if (response.isOk()) {
                JSONObject result = JSONUtil.parseObj(response.body());
                if (result.getInt("errcode") == 0) {
                    String userId = result.getStr("userid");
                    log.info("OAuth获取用户信息成功，userId={}", userId);
                    return userId;
                } else {
                    log.error("OAuth获取用户信息失败，errcode={}, errmsg={}",
                            result.getInt("errcode"), result.getStr("errmsg"));
                }
            }
        } catch (Exception e) {
            log.error("OAuth获取用户信息异常", e);
        }
        return null;
    }

    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待处理";
            case 1: return "处理中";
            case 2: return "已完成";
            case 3: return "已关闭";
            default: return "未知";
        }
    }

    private String getLevelFromReason(String reason) {
        if (reason == null) return "-";
        if (reason.contains("严重")) return "严重";
        if (reason.contains("高")) return "高";
        if (reason.contains("中")) return "中";
        if (reason.contains("低")) return "低";
        return "-";
    }
}
