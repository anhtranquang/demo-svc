package com.finx.contractservice.core.domain;

import com.finx.contractservice.core.enumeration.ContractStatus;
import com.finx.contractservice.core.enumeration.LanguageCode;
import java.time.ZonedDateTime;
import java.util.Map;
import lombok.*;

@Data
@Builder
public class ContractDetail {
    private Long contractId;
    private String contractNo;
    private ContractParty partyDetail;
    private ContractCertificate certificate;
    private Map<String, String> data;
    private ContractStatus status;
    private String signedUrl;
    private String issuer;
    private LanguageCode languageCode;
    private String billCode;
    private ContractTemplate template;
    private ZonedDateTime createdAt;
    private String createdBy;
}
