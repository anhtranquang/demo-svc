package com.finx.contractservice.infra.service;

import static java.util.Objects.isNull;

import com.finx.contractservice.core.domain.BlockingInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CachingBlockingEsignService {

    public static final String TEMPLATE_CACHE_KEY = "contract-service:blocking-cached";

    @Cacheable(value = TEMPLATE_CACHE_KEY, key = "#blockingInfo.partyId", unless = "#result == null")
    public BlockingInfo loadBlockingInfo(BlockingInfo blockingInfo) {
        if (isNull(blockingInfo.getErrorCode())) {
            return null;
        }
        return blockingInfo;
    }
}
