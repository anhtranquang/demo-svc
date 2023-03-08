package com.finx.contractservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ContractParty {
    private Long partyId;
    private String phoneNumber;
    private String email;
    private String nid;
    private String fullName;
    private String citizenId;
    private String location;
    private String stateOrProvince;
    private String country;
    private String frontSideOfIDDocument;
    private String backSideOfIDDocument;
}
