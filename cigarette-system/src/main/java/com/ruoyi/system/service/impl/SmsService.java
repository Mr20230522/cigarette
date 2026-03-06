package com.ruoyi.system.service.impl;

import org.springframework.stereotype.Service;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

@Service
public class SmsService {

    private static final String APPID = "R75bCe1zg1ivtfG9N2Rek8tstwlq39Ku";
    private static final String APPKEY = "PLoqovp88nzCIYILdiVkMnzqsMYGmlNe";
    private static final String SMS_URL = "http://106.58.220.181:9998/yktsms/send";

    public boolean sendSms(String phone, String msg) {
        HttpURLConnection conn = null;
        try {
//            System.out.println("========== 开始发送短信 ==========");
//            System.out.println("手机号: " + phone);
//            System.out.println("短信内容: " + msg);

            // 1. 构建签名
            String signStr = APPID + phone + msg + APPKEY;
//            System.out.println("签名原始字符串: " + signStr);

            String sign = md5(signStr);
//            System.out.println("MD5签名: " + sign);

            // 2. 编码短信内容
            String encodedMsg = URLEncoder.encode(msg, "UTF-8");
//            System.out.println("编码后的短信: " + encodedMsg);

            // 3. 构建完整URL
            String fullUrl = String.format(
                    "%s?appid=%s&mobile=%s&msg=%s&sign=%s",
                    SMS_URL, APPID, phone, encodedMsg, sign
            );
//            System.out.println("完整请求URL: " + fullUrl);

            // 4. 发送请求
            URL url = new URL(fullUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

//            System.out.println("请求方法: " + conn.getRequestMethod());
//            System.out.println("连接超时: 5000ms");
//            System.out.println("读取超时: 5000ms");

            // 5. 获取响应码
            int responseCode = conn.getResponseCode();
//            System.out.println("响应状态码: " + responseCode);

            if (responseCode != 200) {
//                System.out.println("❌ 响应码不是200，可能接口有问题");
                // 尝试读取错误流
                try {
                    BufferedReader errorReader = new BufferedReader(
                            new InputStreamReader(conn.getErrorStream(), "UTF-8")
                    );
                    String errorResponse = errorReader.lines().collect(Collectors.joining());
//                    System.out.println("错误响应内容: " + errorResponse);
                    errorReader.close();
                } catch (Exception e) {
//                    System.out.println("无法读取错误流");
                }
                return false;
            }

            // 6. 读取成功响应
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );
            String response = reader.lines().collect(Collectors.joining());
            reader.close();

//            System.out.println("响应内容: " + response);

            // 7. 解析响应
            String[] parts = response.split(",");
//            System.out.println("响应分割结果: ");
            for (int i = 0; i < parts.length; i++) {
//                System.out.println("  parts[" + i + "] = " + parts[i]);
            }

            boolean success = parts.length >= 3 && "成功".equals(parts[2]);
//            System.out.println("发送结果: " + (success ? "✅ 成功" : "❌ 失败"));

            return success;

        } catch (Exception e) {
//            System.out.println("❌ 发送短信异常: " + e.getMessage());
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
//                System.out.println("连接已断开");
            }
//            System.out.println("========== 短信发送结束 ==========");
        }
    }

    private String md5(String str) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] digest = md.digest(str.getBytes("UTF-8"));
            StringBuilder sb = new StringBuilder();
            for (byte b : digest) {
                sb.append(String.format("%02x", b));
            }
            String result = sb.toString();
//            System.out.println("MD5加密: " + str + " -> " + result);
            return result;
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
}