package com.programmingtechie.zalo_oa_service.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Getter;
import lombok.Setter;

@Component
@Getter
@Setter
@ConfigurationProperties(prefix = "zalo")
public class ZaloConfig {
    private String appId;
    private String oaId;
    private String secretKey;
    private String authorizationCode;
}
