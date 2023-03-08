package com.finx.contractservice.infra.client.base;

import feign.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignLogLevel {

    @Bean
    Logger.Level getLevel() {
        return Logger.Level.FULL;
    }
}
