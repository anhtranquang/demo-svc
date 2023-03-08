package com.finx.contractservice.infra.repository;

import com.finx.contractservice.core.domain.ContractCertificate;
import com.finx.contractservice.core.repository.ContractCertificateRepository;
import com.finx.contractservice.infra.repository.dao.CertificateDao;
import com.finx.contractservice.infra.repository.entity.ContractCertificateEntity;
import com.finx.contractservice.infra.repository.mapper.ContractEntityMapper;
import java.util.Optional;
import org.springframework.stereotype.Component;

@Component
public class ContractCertificateRepositoryImpl implements ContractCertificateRepository {

    private final CertificateDao certificateDao;
    private final ContractEntityMapper contractEntityMapper;

    public ContractCertificateRepositoryImpl(
            CertificateDao certificateDao, ContractEntityMapper contractEntityMapper) {
        this.certificateDao = certificateDao;
        this.contractEntityMapper = contractEntityMapper;
    }

    @Override
    public Optional<ContractCertificate> findActiveContractCertificate(long partyId) {
        Optional<ContractCertificateEntity> entity =
                certificateDao.findValidCertificateByPartyId(partyId);
        return entity.map(contractEntityMapper::toCertificateModal);
    }
}
