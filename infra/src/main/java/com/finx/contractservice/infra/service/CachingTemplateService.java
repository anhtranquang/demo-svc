package com.finx.contractservice.infra.service;

import com.finx.contractservice.core.domain.ContractTemplate;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.IOUtils;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CachingTemplateService {

    public static final String TEMPLATE_CACHE_KEY = "template-cached";

    @Cacheable(value = TEMPLATE_CACHE_KEY, key = "#contractTemplate.templateName")
    public byte[] loadContractTemplate(ContractTemplate contractTemplate) throws IOException {
        Resource resource = new ClassPathResource(contractTemplate.getTemplateUrl());
        return IOUtils.toByteArray(resource.getInputStream());
    }
}
