package com.finx.contractservice.core.repository;

import com.finx.contractservice.core.domain.ContractCertificate;
import java.util.Optional;

public interface ContractCertificateRepository {
    Optional<ContractCertificate> findActiveContractCertificate(long partyId);
}
