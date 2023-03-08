package com.finx.contractservice.core.domain;

import java.time.ZonedDateTime;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ContractCertificate {
    private Long id;
    private ZonedDateTime validFrom;
    private ZonedDateTime validTo;
    private String agreementId;
    private String certificate;
    private String certificateSerialNumber;
    private String certificateDn;
    private String billCode;
    private String createdBy;
    private Long partyId;
    private ZonedDateTime createdAt;
}
