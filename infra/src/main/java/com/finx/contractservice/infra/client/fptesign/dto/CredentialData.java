package com.finx.contractservice.infra.client.fptesign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CredentialData {
    @JsonProperty("username")
    private String username;

    @JsonProperty("password")
    private String password;

    @JsonProperty("signature")
    private String signature;

    @JsonProperty("pkcs1Signature")
    private String pkcs1Signature;

    @JsonProperty("timestamp")
    private String timestamp;
}
