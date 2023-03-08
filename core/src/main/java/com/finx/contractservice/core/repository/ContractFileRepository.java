package com.finx.contractservice.core.repository;

import com.finx.contractservice.core.domain.ContractFileMetadata;
import java.util.Optional;

public interface ContractFileRepository {
    Optional<ContractFileMetadata> findContractFile(Long draftId);

    ContractFileMetadata createDraft(ContractFileMetadata contractDetail);
}
