package com.finx.contractservice.api.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record OtpVerifyRequest(
        @NotBlank String authorizeCode, String issuer, @NotNull Long partyId) {}
