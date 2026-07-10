package com.ruoyi.system.service;

import com.ruoyi.system.domain.TobAlertTask;

/**
 * 企业微信推送服务接口
 */
public interface IWxworkPushService {

    /**
     * 获取企业微信access_token
     */
    String getAccessToken();

    /**
     * 推送TextCard消息
     * @param task   预警任务
     * @param toUser 接收人企业微信用户ID
     * @return 是否推送成功
     */
    boolean pushTextCard(TobAlertTask task, String toUser);

    /**
     * 生成带签名的处理链接
     * @param taskId 任务ID
     * @return 签名链接
     */
    String generateSignedUrl(Long taskId);

    /**
     * 推送Text纯文本消息（不需要域名，仅文字通知）
     * @param task   预警任务
     * @param toUser 接收人企业微信用户ID
     * @return 是否推送成功
     */
    boolean pushText(TobAlertTask task, String toUser);
}
