package com.finx.contractservice.api.dto;

public record GeneratingContractResponse(long draftId, String templateName, String url) {}
