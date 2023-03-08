package com.finx.contractservice.infra.client.fptesign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultipleSignedFileData {
    @JsonProperty("signedFileData")
    private byte[] signedFileData;

    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("signedFileName")
    private String signedFileName;

    @JsonProperty("signedFileUUID")
    private String signedFileUUID;

    @JsonProperty("dmsMetaData")
    private String dmsMetaData;

    @JsonProperty("signatureValue")
    private String signatureValue;
}
