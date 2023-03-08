package com.finx.contractservice.core.service;

import com.finx.contractservice.core.domain.*;
import com.finx.contractservice.core.enumeration.ContractStatus;
import com.finx.contractservice.core.enumeration.LanguageCode;
import com.finx.contractservice.core.exception.ContractExistedException;
import com.finx.contractservice.core.exception.ContractNotFoundException;
import com.finx.contractservice.core.repository.ContractCertificateRepository;
import com.finx.contractservice.core.repository.ContractRepository;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import org.springframework.util.StringUtils;

public class ContractService {

    private final LoanContractClientService contractClientService;
    private final PartyClientService partyClientService;
    private final TemplateService templateService;
    private final StorageClientService storageClientService;
    private final ContractRepository contractRepository;
    private final ContractCertificateRepository contractCertificateRepository;

    public ContractService(
            LoanContractClientService contractClientService,
            ContractRepository contractRepository,
            PartyClientService partyClientService,
            TemplateService templateService,
            StorageClientService storageClientService,
            ContractCertificateRepository contractCertificateRepository) {
        this.contractClientService = contractClientService;
        this.contractRepository = contractRepository;
        this.partyClientService = partyClientService;
        this.templateService = templateService;
        this.storageClientService = storageClientService;
        this.contractCertificateRepository = contractCertificateRepository;
    }

    public ContractDetail requestContractSignCloud(
            String contractNo,
            long partyId,
            String templateName,
            Map<String, String> data,
            Long draftId) {

        Optional<ContractDetail> contract =
                contractRepository.findByContractNoAndPartyId(contractNo, partyId);
        if (contract.isPresent() && ContractStatus.SIGNED.equals(contract.get().getStatus())) {
            throw new ContractExistedException(contract.get().getContractId());
        }

        ContractParty party = partyClientService.getDetailByPartyId(partyId);
        ContractCertificate certificate =
                contractCertificateRepository
                        .findActiveContractCertificate(partyId)
                        .orElseGet(
                                () -> contractClientService.generateCertificateForSignCloud(contractNo, party));
        DocumentFile contractFile =
                generateContractFile(templateName, data, draftId, party.getPartyId());

        if (contract.isEmpty()) {
            ContractDetail contractDetail =
                    ContractDetail.builder()
                            .partyDetail(party)
                            .certificate(certificate)
                            .template(contractFile.getContractTemplate())
                            .languageCode(LanguageCode.VI)
                            .contractNo(contractNo)
                            .data(data)
                            .status(ContractStatus.SIGN_REQUEST)
                            .build();
            String billCode =
                    contractClientService.sendContractForSignCloud(contractDetail, contractFile);
            contractDetail.setBillCode(billCode);

            return contractRepository.createContract(contractDetail);
        }

        ContractDetail contractDetail = contract.get();
        contractDetail.setCertificate(certificate);
        String billCode = contractClientService.sendContractForSignCloud(contractDetail, contractFile);
        contractDetail.setBillCode(billCode);
        contractRepository.createContract(contractDetail);

        return contractDetail;
    }

    private DocumentFile generateContractFile(
            String templateName, Map<String, String> data, Long draftId, long partyId) {

        if (Objects.nonNull(draftId)) {
            return templateService.generateFile(draftId, data, partyId);
        }

        if (Objects.isNull(data) || !StringUtils.hasText(templateName)) {
            throw new NullPointerException("TemplateName or data must not be null");
        }

        ContractTemplate contractTemplate = templateService.getTemplateByName(templateName);
        return templateService.generateFile(contractTemplate, data, partyId);
    }

    public ContractDetail authorizeCounterSigningForSignCloud(
            Long contractId, String authorizeCode, long partyId) {
        Optional<ContractDetail> contract =
                contractRepository.findByContractIdAndPartyId(contractId, partyId);
        if (contract.isEmpty()) {
            throw new ContractNotFoundException(contractId);
        }

        ContractDetail contractDetail = contract.get();
        DocumentFile signedFile =
                contractClientService.authorizeCounterSigningForSignCloud(
                        contractDetail.getCertificate().getAgreementId(),
                        authorizeCode,
                        contractDetail.getBillCode(),
                        partyId);

        // Save file to External Storage;
        String signedUrl = storageClientService.saveSignedContract(signedFile, partyId);

        contractDetail.setSignedUrl(signedUrl);
        contractDetail.setStatus(ContractStatus.SIGNED);
        contractRepository.updateContractStatusAndUrl(
                ContractStatus.SIGNED, signedUrl, contractDetail.getContractId());

        return contractDetail;
    }

    public ContractDetail regenerateAuthorizeCode(Long contractId, long partyId) {
        Optional<ContractDetail> contract =
                contractRepository.findByContractIdAndPartyId(contractId, partyId);
        if (contract.isEmpty()) {
            throw new ContractNotFoundException(contractId);
        }

        if (ContractStatus.SIGNED.equals(contract.get().getStatus())) {
            throw new ContractExistedException("[ContractId = " + contractId + "] was signed.");
        }

        contractClientService.regenerateAuthorizationCodeForSignCloud(
                contract.get().getCertificate().getAgreementId(), partyId);
        return contract.get();
    }
}
