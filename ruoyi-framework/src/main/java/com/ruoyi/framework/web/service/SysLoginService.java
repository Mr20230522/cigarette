package com.ruoyi.framework.web.service;

import javax.annotation.Resource;
import com.ruoyi.common.enums.UserStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import com.ruoyi.common.constant.CacheConstants;
import com.ruoyi.common.constant.Constants;
import com.ruoyi.common.constant.UserConstants;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.core.redis.RedisCache;
import com.ruoyi.common.exception.ServiceException;
import com.ruoyi.common.exception.user.*;
import com.ruoyi.common.utils.*;
import com.ruoyi.common.utils.ip.IpUtils;
import com.ruoyi.framework.manager.AsyncManager;
import com.ruoyi.framework.manager.factory.AsyncFactory;
import com.ruoyi.framework.security.context.AuthenticationContextHolder;
import com.ruoyi.system.service.ISysConfigService;
import com.ruoyi.system.service.ISysMenuService;
import com.ruoyi.system.service.ISysUserService;

import java.util.Set;

/**
 * 登录校验方法
 */
@Component
public class SysLoginService {
    @Autowired
    private TokenService tokenService;

    @Resource
    private AuthenticationManager authenticationManager;

    @Autowired
    private RedisCache redisCache;

    @Autowired
    private ISysUserService userService;

    @Autowired
    private ISysConfigService configService;

    @Autowired
    private ISysMenuService menuService;

    /**
     * 登录验证
     */
    public String login(String username, String password, String code, String uuid) {
        // IP黑名单校验
        checkBlackIPList(username);

        LoginUser loginUser;
        if (password != null) {
            // 密码登录流程
            validateCaptcha(username, code, uuid);
            loginPreCheck(username, password);
            loginUser = passwordLogin(username, password);
        } else {
            // 手机号等其它登录方式
            loginUser = otherLogin(username);
        }

        // 公共后置处理
        handleLoginSuccess(username, loginUser.getUserId());
        return tokenService.createToken(loginUser);
    }

    /**
     * 密码登录
     */
    private LoginUser passwordLogin(String username, String password) {
        try {
            UsernamePasswordAuthenticationToken authenticationToken =
                    new UsernamePasswordAuthenticationToken(username, password);
            AuthenticationContextHolder.setContext(authenticationToken);
            Authentication authentication = authenticationManager.authenticate(authenticationToken);
            return (LoginUser) authentication.getPrincipal();
        } catch (Exception e) {
            handleLoginException(username, e);
            return null; // 不会执行到此处
        } finally {
            AuthenticationContextHolder.clearContext();
        }
    }

    /**
     * 其它登录方式（如手机号）
     */
    private LoginUser otherLogin(String username) {
        // 基础校验
        if (StringUtils.isEmpty(username) ||
                username.length() < UserConstants.USERNAME_MIN_LENGTH ||
                username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new UserNotExistsException();
        }

        // 获取用户信息
        SysUser user = userService.selectUserByLoginName(username);
        System.out.println("获取用户信息username:"+username);
        System.out.println("获取用户信息user:"+user);
        if (user == null) {
            throw new UserNotExistsException();
        }
        System.out.println("user.getStatus()"+user.getStatus());
        System.out.println("!UserStatus.OK.equals(user.getStatus())"+!UserStatus.OK.equals(user.getStatus()));
        if (UserStatus.OK.equals(user.getStatus())) {
            throw new ServiceException("用户已被停用");
        }

        // 获取用户权限
        System.out.println("跳转到用户权限页面");
        System.out.println("user.getUserId()："+user.getUserId());
        Set<String> permissions = menuService.selectMenuPermsByUserId(user.getUserId());
        permissions.add("1");

        System.out.println("用户权限已经设置成功");
        return new LoginUser(user, permissions);
    }

    /**
     * 校验验证码
     */
    public void validateCaptcha(String username, String code, String uuid) {
        if (!configService.selectCaptchaEnabled()) {
            return;
        }

        String verifyKey = CacheConstants.CAPTCHA_CODE_KEY + StringUtils.nvl(uuid, "");
        String captcha = redisCache.getCacheObject(verifyKey);
        redisCache.deleteObject(verifyKey);

        if (captcha == null) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, "user.jcaptcha.expire");
            throw new CaptchaExpireException();
        }
        if (!code.equalsIgnoreCase(captcha)) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, "user.jcaptcha.error");
            throw new CaptchaException();
        }
    }

    /**
     * 登录前置校验
     */
    public void loginPreCheck(String username, String password) {
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(password)) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, "not.null");
            throw new UserNotExistsException();
        }
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH ||
                password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, "user.password.not.match");
            throw new UserPasswordNotMatchException();
        }
        if (username.length() < UserConstants.USERNAME_MIN_LENGTH ||
                username.length() > UserConstants.USERNAME_MAX_LENGTH) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, "user.password.not.match");
            throw new UserPasswordNotMatchException();
        }
        checkBlackIPList(username);
    }

    /**
     * 处理登录成功
     */
    private void handleLoginSuccess(String username, Long userId) {
        recordLoginInfo(username, Constants.LOGIN_SUCCESS, "user.login.success");
        updateLoginInfo(userId);
    }

    /**
     * 记录登录日志
     */
    private void recordLoginInfo(String username, String status, String messageKey) {
        AsyncManager.me().execute(AsyncFactory.recordLogininfor(
                username, status, MessageUtils.message(messageKey)));
    }

    /**
     * 更新登录信息
     */
    private void updateLoginInfo(Long userId) {
        SysUser sysUser = new SysUser();
        sysUser.setUserId(userId);
        sysUser.setLoginIp(IpUtils.getIpAddr());
        sysUser.setLoginDate(DateUtils.getNowDate());
        userService.updateUserProfile(sysUser);
    }

    /**
     * 检查IP黑名单
     */
    private void checkBlackIPList(String username) {
        String blackStr = configService.selectConfigByKey("sys.login.blackIPList");
        if (IpUtils.isMatchedIp(blackStr, IpUtils.getIpAddr())) {
            recordLoginInfo(username, Constants.LOGIN_FAIL, "login.blocked");
            throw new BlackListException();
        }
    }

    /**
     * 处理登录异常
     */
    private void handleLoginException(String username, Exception e) {
        String message = e instanceof BadCredentialsException
                ? "user.password.not.match" : e.getMessage();
        recordLoginInfo(username, Constants.LOGIN_FAIL, message);
        throw new ServiceException(message);
    }
}