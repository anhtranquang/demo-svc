package com.finx.contractservice.infra.repository;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finx.contractservice.core.domain.ContractDetail;
import com.finx.contractservice.core.enumeration.ContractStatus;
import com.finx.contractservice.core.repository.ContractRepository;
import com.finx.contractservice.infra.repository.dao.CertificateDao;
import com.finx.contractservice.infra.repository.dao.ContractDao;
import com.finx.contractservice.infra.repository.dao.TemplateDao;
import com.finx.contractservice.infra.repository.entity.ContractCertificateEntity;
import com.finx.contractservice.infra.repository.entity.ContractEntity;
import com.finx.contractservice.infra.repository.entity.TemplateEntity;
import com.finx.contractservice.infra.repository.mapper.ContractEntityMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class ContractRepositoryImpl implements ContractRepository {

    private final ContractDao contractDao;
    private final CertificateDao certificateDao;
    private final TemplateDao templateDao;
    private final ObjectMapper objectMapper;
    private final ContractEntityMapper contractEntityMapper;

    public ContractRepositoryImpl(
            ContractDao contractDao,
            CertificateDao certificateDao,
            TemplateDao templateDao,
            ObjectMapper objectMapper,
            ContractEntityMapper contractEntityMapper) {
        this.contractDao = contractDao;
        this.certificateDao = certificateDao;
        this.templateDao = templateDao;
        this.objectMapper = objectMapper;
        this.contractEntityMapper = contractEntityMapper;
    }

    @Override
    public Optional<ContractDetail> findByContractNoAndPartyId(String contractNo, long partyId) {
        Optional<ContractEntity> contractEntity =
                contractDao.findByContractNoAndPartyId(contractNo, partyId);
        if (contractEntity.isEmpty()) {
            return Optional.empty();
        }
        ContractDetail contractDetail = contractEntityMapper.toModel(contractEntity.get());
        return Optional.of(contractDetail);
    }

    @Override
    public Optional<ContractDetail> findByContractIdAndPartyId(Long contractId, long partyId) {
        Optional<ContractEntity> contractEntity = contractDao.findByIdAndPartyId(contractId, partyId);
        if (contractEntity.isEmpty()) {
            return Optional.empty();
        }
        ContractDetail contractDetail = contractEntityMapper.toModel(contractEntity.get());
        return Optional.of(contractDetail);
    }

    @Override
    public ContractDetail createContract(ContractDetail contractDetail) {
        return upsertContract(contractDetail);
    }

    @Override
    public void updateContract(ContractDetail contractDetail) {
        upsertContract(contractDetail);
    }

    @Transactional
    @Override
    public void updateContractStatusAndUrl(
            ContractStatus contractStatus, String url, Long contractId) {
        contractDao.updateStatusAndUrl(contractStatus, url, contractId);
    }

    private ContractDetail upsertContract(ContractDetail contractDetail) {
        TemplateEntity templateEntity =
                templateDao.getReferenceById(contractDetail.getTemplate().getId());

        ContractCertificateEntity certificateEntity =
                contractEntityMapper.toCertificateEntity(contractDetail.getCertificate());
        certificateDao.save(certificateEntity);

        ContractEntity contractEntity = new ContractEntity();
        contractEntity.setId(contractDetail.getContractId());
        contractEntity.setContractNo(contractDetail.getContractNo());
        contractEntity.setTemplate(templateEntity);
        contractEntity.setBillCode(contractDetail.getBillCode());
        contractEntity.setStatus(contractDetail.getStatus());
        contractEntity.setSignedUrl(contractDetail.getSignedUrl());
        contractEntity.setLanguageCode(contractDetail.getLanguageCode());
        contractEntity.setData(objectMapper.convertValue(contractDetail.getData(), JsonNode.class));
        contractEntity.setPartyId(contractDetail.getPartyDetail().getPartyId());
        contractEntity.setPartyDetail(
                objectMapper.convertValue(contractDetail.getPartyDetail(), JsonNode.class));
        contractEntity.setCreatedAt(contractDetail.getCreatedAt());
        contractEntity.setCreatedBy(contractDetail.getCreatedBy());
        contractEntity.setCertificate(certificateEntity);
        contractDao.save(contractEntity);

        contractDetail.setContractId(contractEntity.getId());
        return contractDetail;
    }
}
