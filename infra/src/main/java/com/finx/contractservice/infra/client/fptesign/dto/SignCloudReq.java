package com.finx.contractservice.infra.client.fptesign.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import java.util.List;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
public class SignCloudReq {
    private String relyingParty;
    private String relyingPartyBillCode;
    private String agreementUUID;
    private String sharedAgreementUUID;
    private String sharedRelyingParty;
    private String mobileNo;
    private String email;
    private String certificateProfile;
    private AgreementDetails agreementDetails;
    private CredentialData credentialData;
    private String signingFileUUID;
    private byte[] signingFileData;
    private String signingFileName;
    private String mimeType;
    private String notificationTemplate;
    private String notificationSubject;
    private boolean timestampEnabled;
    private boolean ltvEnabled;
    private String language;
    private String authorizeCode;
    private boolean postbackEnabled;
    private boolean noPadding;
    private int authorizeMethod;
    private byte[] uploadingFileData;
    private String downloadingFileUUID;
    private String currentPasscode;
    private String newPasscode;
    private String hash;
    private String hashAlgorithm;
    private String encryption;
    private String billCode;
    private SignCloudMetaData signCloudMetaData;
    private int messagingMode;
    private int sharedMode;
    private String xslTemplateUUID;
    private String xslTemplate;
    private String xmlDocument;
    private boolean p2pEnabled;
    private boolean csrRequired;
    private boolean certificateRequired;
    private boolean keepOldKeysEnabled;
    private boolean revokeOldCertificateEnabled;
    private String certificate;
    private List<MultipleSigningFileData> multipleSigningFileData;
}
