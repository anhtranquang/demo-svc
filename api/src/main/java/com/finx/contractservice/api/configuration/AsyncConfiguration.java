package com.finx.contractservice.api.configuration;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AsyncConfiguration {

    private static final int corePoolSize = 5;
    private static final int maxPoolSize = 10;
    private static final int queueCapacity = 100;

    @Bean("eventTaskPoolExecutor")
    public ThreadPoolExecutor eventTaskPoolExecutor() {
        return new ThreadPoolExecutor(
                corePoolSize, // corePoolSize
                maxPoolSize, // maximumPoolSize
                10, // KeepAliveTime
                TimeUnit.SECONDS,
                new ArrayBlockingQueue<>(queueCapacity));
    }
}
