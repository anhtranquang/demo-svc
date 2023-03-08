package com.finx.contractservice.infra.service;

import com.finx.contractservice.core.domain.ContractParty;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CachingPartyService {

    public static final String TEMPLATE_CACHE_KEY = "contract-service:party-cached";

    @Cacheable(value = TEMPLATE_CACHE_KEY, key = "#contractParty.partyId")
    public ContractParty loadParty(ContractParty contractParty) {
        return contractParty;
    }
}
