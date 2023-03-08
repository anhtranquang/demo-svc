package com.finx.contractservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.util.Map;

public record GeneratingContractRequest(
        @NotBlank String templateName,
        @NotNull Long partyId,
        @NotNull Map<String, String> data,
        Map<String, String> additionData) {}
