package com.ruoyi.system.domain;


import com.ruoyi.common.annotation.DataSource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "spring.sms")
public class ToSMSModel {
    private String accountId;
    private String authToken;
    private String appId;
    private String templateId;

}
