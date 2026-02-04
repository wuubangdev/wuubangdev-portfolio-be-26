package com.wuubangdev.portfolio.infrastructure.global.database;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@Configuration
@EnableJpaAuditing // Kích hoạt tự động ghi nhận thời gian
public class JpaConfig {
}