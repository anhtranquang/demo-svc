package com.finx.contractservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record ContractSignRequest(
        @NotBlank String contractNo,
        String templateName,
        Map<String, String> data,
        Long draftId,
        @NotNull Long partyId) {}
