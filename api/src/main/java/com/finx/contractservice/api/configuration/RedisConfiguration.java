package com.finx.contractservice.api.configuration;

import com.finx.contractservice.infra.service.CachingBlockingEsignService;
import com.finx.contractservice.infra.service.CachingTemplateService;
import java.time.Duration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;

@Configuration
public class RedisConfiguration {
    @Bean
    public RedisCacheConfiguration cacheConfiguration() {
        return RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofMinutes(60))
                .disableCachingNullValues()
                .serializeValuesWith(
                        RedisSerializationContext.SerializationPair.fromSerializer(
                                new GenericJackson2JsonRedisSerializer()));
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer redisCacheManagerBuilderCustomizer(
            @Value("${caching.templateTTL}") long ttlInMillisecond,
            @Value("${caching.blockingEsignTTL}") long blockingEsignTtlInMillisecond) {
        return (builder) ->
                builder
                        .withCacheConfiguration(
                                CachingBlockingEsignService.TEMPLATE_CACHE_KEY,
                                RedisCacheConfiguration.defaultCacheConfig()
                                        .entryTtl(Duration.ofMillis(blockingEsignTtlInMillisecond)))
                        .withCacheConfiguration(
                                CachingTemplateService.TEMPLATE_CACHE_KEY,
                                RedisCacheConfiguration.defaultCacheConfig()
                                        .entryTtl(Duration.ofMillis(ttlInMillisecond)));
    }
}
