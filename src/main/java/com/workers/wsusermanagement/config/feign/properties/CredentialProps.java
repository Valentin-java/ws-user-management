package com.workers.wsusermanagement.config.feign.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth.rest")
public class CredentialProps {
    private String authUrl;
    private String username;
    private String password;
}
