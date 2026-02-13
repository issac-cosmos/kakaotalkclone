package com.example.oauth.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "oauth2.google")
@Data
public class OAuth2Config {
    private String clientId;
    private String clientSecret;
    private String redirectUri;
    private String tokenUri = "https://oauth2.googleapis.com/token";
    private String userInfoUri = "https://www.googleapis.com/oauth2/v2/userinfo";
    private String authorizationUri = "https://accounts.google.com/o/oauth2/v2/auth";
}
