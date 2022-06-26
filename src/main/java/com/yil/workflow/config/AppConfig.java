/*
 * Copyright (c) 2022. Tüm hakları Yasin Yıldırım'a aittir.
 */

package com.yil.workflow.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Configuration
@ComponentScan(basePackages = "com.yil.workflow")
public class AppConfig {

    @Bean
    public ThreadPoolTaskExecutor taskExecutor() {
        ThreadPoolTaskExecutor pool = new ThreadPoolTaskExecutor();
        pool.setCorePoolSize(1000);
        pool.setMaxPoolSize(2000);
        pool.setWaitForTasksToCompleteOnShutdown(true);
        return pool;
    }
}
