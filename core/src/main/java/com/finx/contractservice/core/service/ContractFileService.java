package com.finx.contractservice.core.service;

import com.finx.contractservice.core.domain.*;
import com.finx.contractservice.core.repository.ContractFileRepository;

public class ContractFileService {

    private final ContractFileRepository contractFileRepository;
    private final TemplateService templateService;
    private final StorageClientService storageClientService;
    private final LoanContractClientService contractClientService;
    private static final long EXPIRE_DRAFT_MILLISECOND = 24 * 60 * 60 * 1000; // 1 days

    public ContractFileService(
            ContractFileRepository contractFileRepository,
            TemplateService templateService,
            StorageClientService storageClientService,
            LoanContractClientService contractClientService) {
        this.contractFileRepository = contractFileRepository;
        this.templateService = templateService;
        this.storageClientService = storageClientService;
        this.contractClientService = contractClientService;
    }

    public ContractFileMetadata generate(ContractFileMetadata metadata) {
        contractClientService.validateBlockingUser(metadata.getPartyId());
        ContractTemplate template = templateService.getTemplateByName(metadata.getTemplateName());
        DocumentFile file =
                templateService.generateFile(template, metadata.getData(), metadata.getPartyId());

        String filePath = storageClientService.saveDraftContract(file);
        metadata.setPath(filePath);
        metadata.setTemplateName(template.getTemplateName());
        contractFileRepository.createDraft(metadata);

        String signedUrl = storageClientService.getSignedUrl(filePath, EXPIRE_DRAFT_MILLISECOND);
        metadata.setUrl(signedUrl);
        return metadata;
    }
}
