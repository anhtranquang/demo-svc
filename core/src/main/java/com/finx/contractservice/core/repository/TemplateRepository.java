package com.finx.contractservice.core.repository;

import com.finx.contractservice.core.domain.ContractTemplate;
import java.util.Optional;

public interface TemplateRepository {
    Optional<ContractTemplate> findLatestTemplateByName(String templateName);

    Optional<ContractTemplate> findTemplateById(long id);
}
