package com.finx.contractservice.infra.client.s3;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AmazonConfig {
    @Bean
    public AmazonS3 s3() {
        return AmazonS3ClientBuilder.standard().withRegion(Regions.AP_SOUTHEAST_1).build();
    }
}
