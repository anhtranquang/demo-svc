package com.finx.contractservice.infra.service;

import com.finx.contractservice.core.domain.ContractFileMetadata;
import com.finx.contractservice.core.domain.ContractTemplate;
import com.finx.contractservice.core.domain.DocumentFile;
import com.finx.contractservice.core.exception.ContractNotFoundException;
import com.finx.contractservice.core.repository.ContractFileRepository;
import com.finx.contractservice.core.repository.TemplateRepository;
import com.finx.contractservice.core.service.TemplateService;
import com.finx.contractservice.infra.client.pdfbox.PdfBoxClient;
import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TemplateServiceImpl implements TemplateService {

    private final TemplateRepository templateRepository;
    private final ContractFileRepository contractFileRepository;
    private final CachingTemplateService cachingTemplateService;
    private final PdfBoxClient pdfBoxClient;

    public TemplateServiceImpl(
            TemplateRepository templateRepository,
            ContractFileRepository contractFileRepository,
            CachingTemplateService cachingTemplateService,
            PdfBoxClient pdfBoxClient) {
        this.templateRepository = templateRepository;
        this.contractFileRepository = contractFileRepository;
        this.cachingTemplateService = cachingTemplateService;
        this.pdfBoxClient = pdfBoxClient;
    }

    @SneakyThrows
    @Override
    public DocumentFile generateFile(
            ContractTemplate contractTemplate, Map<String, String> data, long partyId) {
        byte[] fileBytes = cachingTemplateService.loadContractTemplate(contractTemplate);

        ByteArrayOutputStream outputStream =
                (ByteArrayOutputStream) pdfBoxClient.generatePdf(fileBytes, data);

        return new DocumentFile(
                outputStream.toByteArray(),
                contractTemplate.getTemplateName() + "-" + UUID.randomUUID() + ".pdf",
                contractTemplate);
    }

    @SneakyThrows
    @Override
    public DocumentFile generateFile(Long draftId, Map<String, String> additionData, long partyId) {

        ContractFileMetadata draftEntity =
                contractFileRepository
                        .findContractFile(draftId)
                        .orElseThrow(
                                () -> new ContractNotFoundException(String.format("[draftId=%s]", draftId)));
        ContractTemplate contractTemplate =
                templateRepository
                        .findTemplateById(draftEntity.getTemplateId())
                        .orElseThrow(
                                () ->
                                        new ContractNotFoundException(
                                                String.format("[Template Name=%s]", draftEntity.getTemplateName())));

        byte[] fileBytes = cachingTemplateService.loadContractTemplate(contractTemplate);

        Map<String, String> allData = new HashMap<>();
        allData.putAll(draftEntity.getData());
        allData.putAll(additionData);

        ByteArrayOutputStream outputStream =
                (ByteArrayOutputStream) pdfBoxClient.generatePdf(fileBytes, allData);

        return new DocumentFile(
                outputStream.toByteArray(),
                contractTemplate.getTemplateName() + "_" + partyId + "_" + UUID.randomUUID() + ".pdf",
                contractTemplate);
    }

    String getFileName(String templateName, long partyId) {
        return templateName + "_" + partyId + "_" + UUID.randomUUID() + ".pdf";
    }

    @Override
    public ContractTemplate getTemplateByName(String name) {
        return templateRepository.findLatestTemplateByName(name).orElse(null);
    }
}
