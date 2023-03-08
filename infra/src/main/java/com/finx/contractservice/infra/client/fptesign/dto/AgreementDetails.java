package com.finx.contractservice.infra.client.fptesign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AgreementDetails {
    @JsonProperty("personalName")
    private String personalName;

    @JsonProperty("organization")
    private String organization;

    @JsonProperty("organizationUnit")
    private String organizationUnit;

    @JsonProperty("title")
    private String title;

    @JsonProperty("email")
    private String email;

    @JsonProperty("telephoneNumber")
    private String telephoneNumber;

    @JsonProperty("location")
    private String location;

    @JsonProperty("stateOrProvince")
    private String stateOrProvince;

    @JsonProperty("country")
    private String country;

    @JsonProperty("personalID")
    private String personalID;

    @JsonProperty("passportID")
    private String passportID;

    @JsonProperty("citizenID")
    private String citizenID;

    @JsonProperty("taxID")
    private String taxID;

    @JsonProperty("budgetID")
    private String budgetID;

    @JsonProperty("applicationForm")
    private byte[] applicationForm;

    @JsonProperty("requestForm")
    private byte[] requestForm;

    @JsonProperty("authorizeLetter")
    private byte[] authorizeLetter;

    @JsonProperty("photoIDCard")
    private byte[] photoIDCard;

    @JsonProperty("photoFrontSideIDCard")
    private byte[] photoFrontSideIDCard;

    @JsonProperty("photoBackSideIDCard")
    private byte[] photoBackSideIDCard;

    @JsonProperty("photoActivityDeclaration")
    private byte[] photoActivityDeclaration;

    @JsonProperty("photoAuthorizeDelegate")
    private byte[] photoAuthorizeDelegate;
}
