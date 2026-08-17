package com.ruoyi.system.service.impl;


import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ruoyi.system.domain.TobSmsMoRecord;
import com.ruoyi.system.mapper.TobSmsMoRecordMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class SmsService {
    @Autowired
    private TobSmsMoRecordMapper tobSmsMoRecordMapper;
    private static final String APPID = "R75bCe1zg1ivtfG9N2Rek8tstwlq39Ku";
    private static final String APPKEY = "PLoqovp88nzCIYILdiVkMnzqsMYGmlNe";
    private static final String SMS_URL = "http://106.58.220.181:9998/yktsms/send";
    private static final String MO_URL = "http://106.58.220.181:9998/http/mo";

    //    发送短信逻辑
    public boolean sendSms(String phone, String msg) {
        HttpURLConnection conn = null;
        try {
            // 1. 构建签名
            String signStr = APPID + phone + msg + APPKEY;
            String sign = md5(signStr);
            // 2. 编码短信内容
            String encodedMsg = URLEncoder.encode(msg, "UTF-8");
            // 3. 构建完整URL
            String fullUrl = String.format("%s?appid=%s&mobile=%s&msg=%s&sign=%s", SMS_URL, APPID, phone, encodedMsg, sign);
            // 4. 发送请求
            URL url = new URL(fullUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);
            // 5. 获取响应码
            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
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
            // 7. 解析响应
            String[] parts = response.split(",");
            for (int i = 0; i < parts.length; i++) {
//                System.out.println("  parts[" + i + "] = " + parts[i]);
            }
            boolean success = parts.length >= 3 && "成功".equals(parts[2]);
            System.out.println("发送结果: " + (success ? "✅ 成功" : "❌ 失败"));
            return success;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }
    }

    //    生成MD5加密签名
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

    //    接收短信逻辑
    public void pullUpstreamMessages() {
        HttpURLConnection connection = null;
        try {
            // 1. 准备参数
            int size = 200;
            long time = System.currentTimeMillis() / 1000;
            String sign = md5(APPID + size + time + APPKEY);

            // 2. 构建 POST 请求体 (application/x-www-form-urlencoded)
//            String body = "appid=" + APPID
//                    + "&size=" + size
//                    + "&time=" + time
//                    + "&sign=" + sign;
            String fullUrl = String.format("%s?appid=%s&size=%s&time=%s&sign=%s", MO_URL, APPID, size, time, sign);
            // 3. 发起 HTTP 请求
            URL url = new URL(fullUrl);
            connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("POST");
//            connection.setDoOutput(true); // 允许写入请求体
            connection.setConnectTimeout(10000); // 10秒连接超时
            connection.setReadTimeout(10000);    // 10秒读取超时
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");

            // 写入请求体
//            try (OutputStream os = connection.getOutputStream()) {
//                byte[] input = body.getBytes(StandardCharsets.UTF_8);
//                os.write(input, 0, input.length);
//            } catch (Exception ignored) {
//            }
            // 4. 获取响应
            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                StringBuilder response = new StringBuilder();
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(connection.getInputStream(), StandardCharsets.UTF_8))) {
                    String line;
                    while ((line = br.readLine()) != null) {
                        response.append(line);
                    }
                }
                // 5. 解析 JSON 并处理数据
                String jsonResult = response.toString();
                System.out.println("拉取结果: " + jsonResult);
//                遍历响应结果，解析出每一条数据，插入到数据库
                JSONObject obj = JSON.parseObject(jsonResult);
                if (obj.getInteger("resultCode") == 0) {
                    JSONArray list = obj.getJSONArray("list");
                    if (list == null || list.isEmpty()) {
                        return;
                    } else {
                        List<TobSmsMoRecord> records = new ArrayList<>(list.size());
                        for (int i = 0; i < list.size(); i++) {
                            JSONObject item = list.getJSONObject(i);

                            TobSmsMoRecord record = new TobSmsMoRecord();
                            record.setTaskId(item.getString("taskId"));
                            record.setMobile(item.getString("mobile"));
                            record.setServicenumber(item.getString("servicenumber"));
                            record.setMsgid(item.getString("msgid"));
                            record.setMsg(item.getString("msg"));
                            record.setUptime(item.getString("uptime"));
                            // 默认状态为 0（待处理），插入时无需显式设置，数据库默认值就是 0
                            record.setStatus(0);
                            records.add(record);
                        }
                        // 插入数据库
                        tobSmsMoRecordMapper.insertBatch(records);
                    }
                }
            } else {
//                System.err.println("HTTP 请求失败，响应码: " + responseCode);
            }
        } catch (Exception e) {
//            System.err.println("轮询上行短信发生异常: " + e.getMessage());
            e.printStackTrace();
        } finally {
            if (connection != null) {
                connection.disconnect(); // 关闭连接
            }
        }
    }

    // 处理短信返回数据
    public void handleMessage() {
//        编写一个函数，用于读取tob_sms_mo_record表和
    }
}