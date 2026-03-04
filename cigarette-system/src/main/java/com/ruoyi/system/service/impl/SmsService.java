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
            String signStr = APPID + phone + msg + APPKEY;
            String sign = md5(signStr);
            String encodedMsg = URLEncoder.encode(msg, "UTF-8");
            String fullUrl = String.format(
                    "%s?appid=%s&mobile=%s&msg=%s&sign=%s",
                    SMS_URL, APPID, phone, encodedMsg, sign
            );

            URL url = new URL(fullUrl);
            conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setConnectTimeout(5000);
            conn.setReadTimeout(5000);
            conn.setDoOutput(true);

            int responseCode = conn.getResponseCode();
            if (responseCode != 200) {
                return false;
            }

            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), "UTF-8")
            );
            String response = reader.lines().collect(Collectors.joining());
            reader.close();

            String[] parts = response.split(",");
            return parts.length >= 3 && "成功".equals(parts[2]);

        } catch (Exception e) {
            e.printStackTrace();
            return false;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
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
            return sb.toString();
        } catch (Exception e) {
            throw new RuntimeException("MD5加密失败", e);
        }
    }
}