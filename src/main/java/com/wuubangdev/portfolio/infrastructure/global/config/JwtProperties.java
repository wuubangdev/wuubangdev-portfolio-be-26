package com.wuubangdev.portfolio.infrastructure.global.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.jwt") // Ánh xạ các dòng có tiền tố app.jwt
@Getter
@Setter
public class JwtProperties {

    // Spring sẽ tự động hiểu app.jwt.secret -> secret
    private String secret;

    // Spring sẽ tự động hiểu app.jwt.expiration-ms -> expirationMs
    private long expirationMs;
}