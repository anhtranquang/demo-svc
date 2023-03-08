package com.finx.contractservice.core.service;

import com.finx.contractservice.core.domain.ContractTemplate;
import com.finx.contractservice.core.domain.DocumentFile;
import java.util.Map;

public interface TemplateService {
    DocumentFile generateFile(
            ContractTemplate contractTemplate, Map<String, String> data, long partyId);

    DocumentFile generateFile(Long draftId, Map<String, String> additionData, long partyId);

    ContractTemplate getTemplateByName(String name);
}
