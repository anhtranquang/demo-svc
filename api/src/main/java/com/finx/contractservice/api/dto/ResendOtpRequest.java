package com.finx.contractservice.api.dto;

import jakarta.validation.constraints.NotBlank;

public record ResendOtpRequest(@NotBlank String contractId) {}
