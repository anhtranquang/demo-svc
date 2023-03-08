package com.finx.contractservice.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;

@EnableCaching
@EnableAsync
@SpringBootApplication(scanBasePackages = "com.finx.contractservice")
@EnableFeignClients(basePackages = "com.finx.contractservice.infra")
@EnableJpaRepositories(basePackages = "com.finx.contractservice.infra.repository")
@EntityScan(basePackages = "com.finx.contractservice.infra.repository.entity")
public class ContractServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(ContractServiceApplication.class, args);
    }
}
