package com.example.wallet_service.config;

import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class StartupLogger {

    private final Environment env;

    @PostConstruct
    public void logConfiguration() {
        log.info("==== Application Configuration ====");
        log.info("Server Port: {}", env.getProperty("server.port"));
        log.info("Tomcat Max Threads: {}", env.getProperty("server.tomcat.threads.max"));
        log.info("Tomcat Min Spare Threads: {}", env.getProperty("server.tomcat.threads.min-spare"));
        log.info("Tomcat Accept Count: {}", env.getProperty("server.tomcat.accept-count"));
        log.info("Tomcat Connection Timeout: {}", env.getProperty("server.tomcat.connection-timeout"));

        log.info("Datasource URL: {}", env.getProperty("spring.datasource.url"));
        log.info("Datasource Username: {}", env.getProperty("spring.datasource.username"));
        log.info("Hikari Max Pool Size: {}", env.getProperty("spring.datasource.hikari.maximum-pool-size"));
        log.info("Hikari Min Idle: {}", env.getProperty("spring.datasource.hikari.minimum-idle"));
        log.info("Hikari Idle Timeout: {}", env.getProperty("spring.datasource.hikari.idle-timeout"));
        log.info("Hikari Connection Timeout: {}", env.getProperty("spring.datasource.hikari.connection-timeout"));
        log.info("===================================");
    }
}