package com.ai.sync_law.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "com.ai.sync_law")
public class OpenFeignConfig {
}
