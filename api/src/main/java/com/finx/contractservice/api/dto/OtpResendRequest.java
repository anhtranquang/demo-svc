package com.finx.contractservice.api.dto;

import jakarta.validation.constraints.NotNull;

public record OtpResendRequest(String issuer, @NotNull Long partyId) {}
