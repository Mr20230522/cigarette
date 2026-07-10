package com.ruoyi.web.controller.cigarette.alert;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.TobAlertTask;
import com.ruoyi.system.service.ITobAlertTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 企业微信OAuth认证Controller
 * 用户点击企微消息链接后，进行身份认证和权限验证
 */
@RestController
@RequestMapping("/wxwork")
public class TobWxworkAuthController extends BaseController {

    private static final String SIGN_SALT = "AlertTask2026";

    @Autowired
    private ITobAlertTaskService tobAlertTaskService;

    /**
     * 企业微信OAuth授权跳转
     * GET /wxwork/authorize?redirect_url=xxx
     */
    @GetMapping("/authorize")
    public void authorize(@RequestParam("redirect_url") String redirectUrl,
                          HttpServletResponse response) throws IOException {
        // 获取企微CorpID
        String corpId = "wx_default_corpid"; // 需要替换为实际CorpID
        String oauthUrl = "https://open.weixin.qq.com/connect/oauth2/authorize"
                + "?appid=" + corpId
                + "&redirect_uri=" + java.net.URLEncoder.encode(redirectUrl, "UTF-8")
                + "&response_type=code"
                + "&scope=snsapi_base"
                + "&state=STATE#wechat_redirect";
        response.sendRedirect(oauthUrl);
    }

    /**
     * 企业微信OAuth回调
     * GET /wxwork/callback?code=xxx&state=xxx
     */
    @GetMapping("/callback")
    public AjaxResult callback(@RequestParam("code") String code,
                               @RequestParam(value = "state", required = false) String state) {
        // TODO: 使用code换取用户身份信息（UserID）
        // 此处简化处理，实际需调用企微API:
        // https://qyapi.weixin.qq.com/cgi-bin/auth/getuserinfo?access_token=ACCESS_TOKEN&code=CODE
        return success("认证成功");
    }

    /**
     * 任务处理页面入口（企微内嵌浏览器访问）
     * GET /wxwork/handle?task_id=xxx&timestamp=xxx&sign=xxx
     */
    @GetMapping("/handle")
    public void handle(@RequestParam("task_id") Long taskId,
                       @RequestParam("timestamp") Long timestamp,
                       @RequestParam("sign") String sign,
                       HttpServletResponse response) throws IOException {
        // 1. 验证签名
        if (!verifySign(taskId, timestamp, sign)) {
            response.getWriter().write("<h2>链接已过期或无效</h2>");
            return;
        }

        // 2. 验证时效（5分钟有效期）
        long now = System.currentTimeMillis() / 1000;
        if (now - timestamp > 300) {
            response.getWriter().write("<h2>链接已过期（超过5分钟）</h2>");
            return;
        }

        // 3. 查询任务
        TobAlertTask task = tobAlertTaskService.selectTobAlertTaskById(taskId);
        if (task == null) {
            response.getWriter().write("<h2>任务不存在</h2>");
            return;
        }

        // 4. 返回处理页面（实际项目中应返回前端页面）
        response.setContentType("text/html;charset=UTF-8");
        response.getWriter().write("<!DOCTYPE html><html><head><meta charset=\"UTF-8\">"
                + "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,user-scalable=0\">"
                + "<title>预警处理</title></head><body>"
                + "<h3>预警任务处理</h3>"
                + "<p>车牌：" + task.getPlate() + "</p>"
                + "<p>原因：" + task.getReason() + "</p>"
                + "<p>卡口：" + task.getCameraName() + "</p>"
                + "<p>状态：" + getStatusText(task.getStatus()) + "</p>"
                + "</body></html>");
    }

    /**
     * 验证签名
     */
    private boolean verifySign(Long taskId, Long timestamp, String sign) {
        String raw = taskId + "|" + timestamp + "|" + SIGN_SALT;
        HMac hmac = new HMac(HmacAlgorithm.HmacSHA256, SIGN_SALT.getBytes());
        String expectedSign = Base64.encodeUrlSafe(hmac.digestHex(raw));
        return expectedSign.equals(sign);
    }

    /**
     * 获取状态文本
     */
    private String getStatusText(Integer status) {
        if (status == null) return "未知";
        switch (status) {
            case 0: return "待处理";
            case 1: return "已接受";
            case 2: return "处理中(已反馈)";
            case 3: return "已关闭";
            default: return "未知";
        }
    }
}
