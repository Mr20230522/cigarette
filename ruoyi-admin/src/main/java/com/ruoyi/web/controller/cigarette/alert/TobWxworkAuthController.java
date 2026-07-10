package com.ruoyi.web.controller.cigarette.alert;

import cn.hutool.core.codec.Base64;
import cn.hutool.crypto.digest.HMac;
import cn.hutool.crypto.digest.HmacAlgorithm;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.system.domain.TobAlertTask;
import com.ruoyi.system.domain.TobRegionPerson;
import com.ruoyi.system.service.ITobAlertTaskService;
import com.ruoyi.system.service.IWxworkPushService;
import com.ruoyi.system.service.ITobWxworkConfigService;
import com.ruoyi.system.service.ITobRegionPersonService;
import com.ruoyi.system.service.ISysUserService;
import com.ruoyi.system.service.ISysRoleService;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.entity.SysRole;
import com.ruoyi.framework.web.service.SysLoginService;
import cn.hutool.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    private static final Logger log = LoggerFactory.getLogger(TobWxworkAuthController.class);
    private static final String SIGN_SALT = "AlertTask2026";

    @Autowired
    private ITobAlertTaskService tobAlertTaskService;

    @Autowired
    private IWxworkPushService wxworkPushService;

    @Autowired
    private ITobWxworkConfigService wxworkConfigService;

    @Autowired
    private SysLoginService loginService;

    @Autowired
    private ISysUserService sysUserService;

    @Autowired
    private ISysRoleService sysRoleService;

    @Autowired
    private ITobRegionPersonService regionPersonService;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    /**
     * 企业微信OAuth授权跳转
     * GET /wxwork/authorize?redirect_url=xxx
     */
    @GetMapping("/authorize")
    public void authorize(@RequestParam("redirect_url") String redirectUrl,
                          HttpServletResponse response) throws IOException {
        String corpId = wxworkConfigService.getConfigValue("wxwork.corpid", "");
        if (corpId == null || corpId.isEmpty()) {
            response.setContentType("application/json;charset=UTF-8");
            response.getWriter().write("{\"code\":500,\"msg\":\"CorpID未配置，请联系管理员\"}");
            return;
        }
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
        if (code == null || code.isEmpty()) {
            return error("缺少code参数");
        }
        String userId = wxworkPushService.getUserInfoByCode(code);
        if (userId == null || userId.isEmpty()) {
            return error("获取用户信息失败，请重试");
        }
        log.info("OAuth认证成功，企微userId={}", userId);
        return success("认证成功", userId);
    }

    /**
     * 企微OAuth登录——code换JWT token
     * GET /wxwork/auth/login?code=xxx
     * 内部完成：code→userId（调用企微API）→免密登录（RuoYi otherLogin）→返回JWT
     */
    @GetMapping("/auth/login")
    public AjaxResult wxworkLogin(@RequestParam("code") String code) {
        // 第一步：code换企微userId
        String wxworkUserId = wxworkPushService.getUserInfoByCode(code);
        if (wxworkUserId == null || wxworkUserId.isEmpty()) {
            return error("获取企微用户信息失败，请重试");
        }

        // 第二步：尝试免密登录
        String token;
        try {
            token = loginService.login(wxworkUserId, null, null, null);
        } catch (Exception e) {
            // 用户不存在 → 自动注册
            log.info("OAuth用户{}不存在，尝试自动注册", wxworkUserId);
            try {
                autoRegisterWxworkUser(wxworkUserId);
                token = loginService.login(wxworkUserId, null, null, null);
            } catch (Exception e2) {
                log.error("自动注册后登录失败，wxworkUserId={}, error={}", wxworkUserId, e2.getMessage());
                return error("登录失败：" + e2.getMessage());
            }
        }

        JSONObject result = new JSONObject();
        result.set("token", token);
        result.set("wxworkUserId", wxworkUserId);
        log.info("企微OAuth登录成功，wxworkUserId={}", wxworkUserId);
        return success("登录成功", result);
    }

    /**
     * 自动注册企微用户：查昵称 → 创建sys_user → 分配角色
     */
    private void autoRegisterWxworkUser(String wxworkUserId) {
        // 1. 从 tob_region_person 获取昵称
        String nickName = wxworkUserId;
        TobRegionPerson person = regionPersonService.selectDistinctByUserId(wxworkUserId);
        if (person != null && person.getUserName() != null && !person.getUserName().isEmpty()) {
            nickName = person.getUserName();
        }

        // 2. 创建 SysUser
        SysUser user = new SysUser();
        user.setUserName(wxworkUserId);
        user.setNickName(nickName);
        user.setPassword(passwordEncoder.encode(wxworkUserId + "_wxwork_default"));
        user.setStatus("0");
        user.setUserType("00");
        user.setCreateBy("system");

        int rows = sysUserService.insertUser(user);
        if (rows == 0) {
            throw new RuntimeException("插入sys_user失败");
        }
        log.info("自动创建sys_user成功，username={}, nickName={}, userId={}", wxworkUserId, nickName, user.getUserId());

        // 3. 分配默认角色
        SysRole role = sysRoleService.selectRoleById(2L);
        if (role != null) {
            sysUserService.insertUserAuth(user.getUserId(), new Long[]{2L});
            log.info("已为用户{}分配角色：{}", wxworkUserId, role.getRoleName());
        } else {
            log.warn("默认角色(role_id=2)不存在，未分配角色给用户{}", wxworkUserId);
        }
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
