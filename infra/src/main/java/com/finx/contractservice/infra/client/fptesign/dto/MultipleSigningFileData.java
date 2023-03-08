package com.finx.contractservice.infra.client.fptesign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class MultipleSigningFileData {
    @JsonProperty("signingFileData")
    private byte[] signingFileData;

    @JsonProperty("signingFileName")
    private String signingFileName;

    @JsonProperty("mimeType")
    private String mimeType;

    @JsonProperty("xslTemplate")
    private String xslTemplate;

    @JsonProperty("xslTemplate")
    private String xmlDocument;

    @JsonProperty("signCloudMetaData")
    private SignCloudMetaData signCloudMetaData;

    @JsonProperty("hash")
    private String hash;
}
