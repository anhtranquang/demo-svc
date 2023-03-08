package com.finx.contractservice.core.repository;

import com.finx.contractservice.core.domain.ContractDetail;
import com.finx.contractservice.core.enumeration.ContractStatus;
import java.util.Optional;

public interface ContractRepository {
    Optional<ContractDetail> findByContractNoAndPartyId(String contractNo, long partyId);

    Optional<ContractDetail> findByContractIdAndPartyId(Long contractId, long partyId);

    ContractDetail createContract(ContractDetail contractDetail);

    void updateContract(ContractDetail contractDetail);

    void updateContractStatusAndUrl(ContractStatus contractStatus, String url, Long contractId);
}
