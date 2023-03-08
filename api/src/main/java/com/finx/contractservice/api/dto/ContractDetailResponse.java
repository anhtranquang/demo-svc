package com.finx.contractservice.api.dto;

import com.finx.contractservice.core.enumeration.ContractStatus;

public record ContractDetailResponse(
        long contractId, String contractNo, ContractStatus status, String signedUrl) {}
