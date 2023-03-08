package com.finx.contractservice.infra.service;

import static java.util.Objects.nonNull;

import com.finx.contractservice.common.utils.FileUtil;
import com.finx.contractservice.core.domain.BlockingInfo;
import com.finx.contractservice.core.domain.ContractCertificate;
import com.finx.contractservice.core.domain.ContractDetail;
import com.finx.contractservice.core.domain.ContractParty;
import com.finx.contractservice.core.domain.DocumentFile;
import com.finx.contractservice.core.exception.DomainCode;
import com.finx.contractservice.core.exception.DomainException;
import com.finx.contractservice.core.service.LoanContractClientService;
import com.finx.contractservice.infra.client.fptesign.FptESignFeignClient;
import com.finx.contractservice.infra.client.fptesign.constant.ESignCloudConstant;
import com.finx.contractservice.infra.client.fptesign.dto.*;
import com.finx.contractservice.infra.client.fptesign.utils.PKCS12Util;
import com.finx.contractservice.infra.event.dto.ContractSignedEvent;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class FptEsignClientService implements LoanContractClientService {

    private final FptESignFeignClient feignClient;
    private final ApplicationEventPublisher publisher;
    private static final String CERTIFICATE_PROFILE = "PERS.1D";
    private final String notificationTemplate;
    public final String notificationSubject;
    private final String partyUser;
    private final String partyPassword;
    private final String partySignature;
    private final String partyKeyStore;
    private final String partyKeyStorePassword;
    private final CachingBlockingEsignService cachingBlockingEsignService;

    private static Map<Integer, DomainCode> fptErrorCodeMap =
            Map.of(
                    1039, DomainCode.NUMBER_OF_ALLOWED_OTP_GENERATION_EXCEEDED,
                    1004, DomainCode.INVALID_OTP,
                    1005, DomainCode.INVALID_OTP_MANY_TIME,
                    1006, DomainCode.OTP_TIMEOUT);

    public FptEsignClientService(
            FptESignFeignClient feignClient,
            ApplicationEventPublisher publisher,
            CachingBlockingEsignService cachingBlockingEsignService,
            @Value("${client.fptEsign.auth.partyUser}") String partyUser,
            @Value("${client.fptEsign.auth.partyPassword}") String partyPassword,
            @Value("${client.fptEsign.auth.partySignature}") String partySignature,
            @Value("${client.fptEsign.auth.keyStore}") String partyKeyStore,
            @Value("${client.fptEsign.auth.keyStorePassword}") String partyKeyStorePassword,
            @Value("${client.fptEsign.notification.template}") String notificationTemplate,
            @Value("${client.fptEsign.notification.subject}") String notificationSubject) {
        this.feignClient = feignClient;
        this.publisher = publisher;
        this.partyUser = partyUser;
        this.partyPassword = partyPassword;
        this.partySignature = partySignature;
        this.partyKeyStore = partyKeyStore;
        this.partyKeyStorePassword = partyKeyStorePassword;
        this.notificationTemplate = notificationTemplate;
        this.notificationSubject = notificationSubject;
        this.cachingBlockingEsignService = cachingBlockingEsignService;
    }

    @Override
    public ContractCertificate generateCertificateForSignCloud(
            String contractNo, ContractParty partyDetail) {

        log.info(
                "FPT-Esign generateCertificateForSignCloud [contractNo={}, partyId={}]",
                contractNo,
                partyDetail.getPartyId());
        CredentialData credentialData = createCredentialData();
        AgreementDetails agreementDetails =
                AgreementDetails.builder()
                        .personalName(partyDetail.getFullName())
                        .personalID(partyDetail.getNid())
                        .citizenID(partyDetail.getCitizenId())
                        .location(partyDetail.getLocation())
                        .stateOrProvince(partyDetail.getStateOrProvince())
                        .country(partyDetail.getCountry())
                        .photoFrontSideIDCard(FileUtil.getByteArray(partyDetail.getFrontSideOfIDDocument()))
                        .photoBackSideIDCard(FileUtil.getByteArray(partyDetail.getBackSideOfIDDocument()))
                        .build();

        SignCloudReq signCloudReq =
                SignCloudReq.builder()
                        .relyingParty(partyUser)
                        .agreementUUID(contractNo)
                        .email(partyDetail.getEmail())
                        .mobileNo(partyDetail.getPhoneNumber())
                        .certificateProfile(CERTIFICATE_PROFILE)
                        .agreementDetails(agreementDetails)
                        .credentialData(credentialData)
                        .build();

        SignCloudResp signCloudResp = feignClient.prepareCertificateForSignCloud(signCloudReq);
        handleErrorResponse(signCloudResp, partyDetail.getPartyId());

        return ContractCertificate.builder()
                .certificate(signCloudResp.getCertificate())
                .certificateDn(signCloudResp.getCertificateDN())
                .validTo(signCloudResp.getValidTo())
                .validFrom(signCloudResp.getValidFrom())
                .billCode(signCloudResp.getBillCode())
                .partyId(partyDetail.getPartyId())
                .agreementId(contractNo)
                .build();
    }

    private void handleErrorResponse(SignCloudResp signCloudResp, Long partyId) {
        int responseCode = signCloudResp.getResponseCode();
        DomainCode domainCode = fptErrorCodeMap.get(responseCode);
        if (nonNull(partyId) && CollectionUtils.containsAny(List.of(1005, 1039), responseCode)) {
            cachingBlockingEsignService.loadBlockingInfo(
                    BlockingInfo.builder()
                            .partyId(partyId)
                            .errorCode(signCloudResp.getResponseCode())
                            .build());
        }
        if (nonNull(domainCode)) {
            throw new DomainException(domainCode);
        }
        if (CollectionUtils.containsAny(List.of(0, 1010, 1007), responseCode)) {
            log.debug("FPT-Esign request successfully.");
        } else {
            log.error("FPT-Esign request fail [error={}]", signCloudResp);
            throw new IllegalStateException("Unexpected value: " + responseCode);
        }
    }

    @Override
    public String sendContractForSignCloud(ContractDetail contract, DocumentFile contractFile) {

        log.info(
                "FPT-Esign sendContractForSignCloud [contractId={}, contractFile={}]",
                contract.getContractId(),
                contractFile.getFileName());

        HashMap<String, String> singletonSigningForItem01 = new HashMap<>();
        singletonSigningForItem01.put("COUNTERSIGNENABLED", "True");
        singletonSigningForItem01.put("PAGENO", "5");
        singletonSigningForItem01.put("POSITIONIDENTIFIER", "BÊN VAY");
        singletonSigningForItem01.put("RECTANGLEOFFSET", "-100,-250");
        singletonSigningForItem01.put("RECTANGLESIZE", "210, 120");
        singletonSigningForItem01.put("VISIBLESIGNATURE", "True");
        singletonSigningForItem01.put("VISUALSTATUS", "False");
        singletonSigningForItem01.put("SHOWSIGNERINFO", "True");
        singletonSigningForItem01.put("SIGNERINFOPREFIX", "Ký bởi:");
        singletonSigningForItem01.put("SHOWDATETIME", "True");
        singletonSigningForItem01.put("DATETIMEPREFIX", "Ký ngày:");
        singletonSigningForItem01.put("SHOWREASON", "True");
        singletonSigningForItem01.put("SIGNREASONPREFIX", "Lý do:");
        singletonSigningForItem01.put("SIGNREASON", "Đồng ý");
        singletonSigningForItem01.put("SHOWLOCATION", "True");
        singletonSigningForItem01.put("LOCATION", "TP. Hồ Chí Minh");
        singletonSigningForItem01.put("LOCATIONPREFIX", "Nơi ký:");
        singletonSigningForItem01.put("TEXTCOLOR", "black");
        singletonSigningForItem01.put("IMAGEANDTEXT", "False");

        HashMap<String, String> counterSigningForItem01 = new HashMap<>();
        counterSigningForItem01.put("PAGENO", "5");
        counterSigningForItem01.put("POSITIONIDENTIFIER", "HDBANK");
        counterSigningForItem01.put("RECTANGLEOFFSET", "-100,-250");
        counterSigningForItem01.put("RECTANGLESIZE", "240,150");
        counterSigningForItem01.put("VISIBLESIGNATURE", "True");
        counterSigningForItem01.put("VISUALSTATUS", "False");
        counterSigningForItem01.put("SHOWSIGNERINFO", "True");
        counterSigningForItem01.put("SIGNERINFOPREFIX", "Ký bởi:");
        counterSigningForItem01.put("SHOWDATETIME", "True");
        counterSigningForItem01.put("DATETIMEPREFIX", "Ký ngày:");
        counterSigningForItem01.put("SHOWREASON", "True");
        counterSigningForItem01.put("SIGNREASONPREFIX", "Lý do:");
        counterSigningForItem01.put("SIGNREASON", "Tôi đồng ý");
        counterSigningForItem01.put("SHOWLOCATION", "True");
        counterSigningForItem01.put("LOCATION", "Hà Nội");
        counterSigningForItem01.put("LOCATIONPREFIX", "Nơi ký:");
        counterSigningForItem01.put("TEXTCOLOR", "black");

        SignCloudMetaData signCloudMetaDataForItem01 =
                SignCloudMetaData.builder()
                        .counterSigning(counterSigningForItem01)
                        .singletonSigning(singletonSigningForItem01)
                        .build();

        SignCloudReq signCloudReq =
                SignCloudReq.builder()
                        .relyingParty(partyUser)
                        .agreementUUID(contract.getCertificate().getAgreementId())
                        .authorizeMethod(ESignCloudConstant.AUTHORISATION_METHOD_SMS)
                        .messagingMode(ESignCloudConstant.ASYNCHRONOUS_CLIENTSERVER)
                        .notificationTemplate(this.notificationTemplate)
                        .notificationSubject(this.notificationSubject)
                        .signCloudMetaData(signCloudMetaDataForItem01)
                        // TODO Prepare file from DMS
                        .signingFileData(contractFile.getFile())
                        .signingFileName(contractFile.getFileName())
                        .credentialData(createCredentialData())
                        .mimeType(ESignCloudConstant.MIMETYPE_PDF)
                        .build();

        SignCloudResp signCloudResp = feignClient.prepareFileForSignCloud(signCloudReq);
        handleErrorResponse(signCloudResp, null);

        return signCloudResp.getBillCode();
    }

    @Override
    public DocumentFile authorizeCounterSigningForSignCloud(
            String agreementId, String authorizeCode, String billCode, Long partyId) {

        log.info(
                "FPT-Esign authorizeCounterSigningForSignCloud [agreementId={}, billCode={}]",
                agreementId,
                billCode);

        SignCloudReq signCloudReq =
                SignCloudReq.builder()
                        .relyingParty(partyUser)
                        .agreementUUID(agreementId)
                        .messagingMode(ESignCloudConstant.SYNCHRONOUS)
                        .billCode(billCode)
                        .authorizeCode(authorizeCode)
                        .credentialData(createCredentialData())
                        .build();

        SignCloudResp signCloudResp = feignClient.authorizeCounterSigningForSignCloud(signCloudReq);
        handleErrorResponse(signCloudResp, partyId);

        if (Objects.isNull(signCloudResp.getSignedFileData())) {
            return new DocumentFile(new byte[0], signCloudResp.getSignedFileName());
        }

        publisher.publishEvent(new ContractSignedEvent(this, agreementId));
        return new DocumentFile(signCloudResp.getSignedFileData(), signCloudResp.getSignedFileName());
    }

    @Override
    public void regenerateAuthorizationCodeForSignCloud(String agreementId, Long partyId) {
        SignCloudReq signCloudReq =
                SignCloudReq.builder()
                        .relyingParty(partyUser)
                        .agreementUUID(agreementId)
                        .authorizeMethod(ESignCloudConstant.AUTHORISATION_METHOD_SMS)
                        .notificationTemplate(this.notificationTemplate)
                        .notificationSubject(this.notificationSubject)
                        .credentialData(createCredentialData())
                        .build();

        SignCloudResp signCloudResp = feignClient.regenerateAuthorizationCodeForSignCloud(signCloudReq);
        handleErrorResponse(signCloudResp, partyId);
    }

    @Override
    public ContractCertificate getCertificateDetailForSignCloud(String agreementId) {
        SignCloudReq signCloudReq =
                SignCloudReq.builder()
                        .relyingParty(partyUser)
                        .agreementUUID(agreementId)
                        .credentialData(createCredentialData())
                        .build();

        SignCloudResp signCloudResp = feignClient.getCertificateDetailForSignCloud(signCloudReq);
        handleErrorResponse(signCloudResp, null);

        return ContractCertificate.builder()
                .certificate(signCloudResp.getCertificate())
                .certificateDn(signCloudResp.getCertificateDN())
                .validTo(signCloudResp.getValidTo())
                .validFrom(signCloudResp.getValidFrom())
                .billCode(signCloudResp.getBillCode())
                .agreementId(agreementId)
                .build();
    }

    @Override
    public void validateBlockingUser(Long partyId) {
        BlockingInfo blockingInfo =
                cachingBlockingEsignService.loadBlockingInfo(
                        BlockingInfo.builder().partyId(partyId).build());
        if (nonNull(blockingInfo)) {
            DomainCode domainCode = fptErrorCodeMap.get(blockingInfo.getErrorCode());
            if (nonNull(domainCode)) {
                throw new DomainException(domainCode);
            }
        }
    }

    private CredentialData createCredentialData() {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String data2sign = partyUser + partyPassword + partySignature + timestamp;
        String pkcs1Signature = null;
        try {
            pkcs1Signature = PKCS12Util.getSignature(data2sign, partyKeyStore, partyKeyStorePassword);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return CredentialData.builder()
                .username(partyUser)
                .password(partyPassword)
                .timestamp(timestamp)
                .signature(partySignature)
                .pkcs1Signature(pkcs1Signature)
                .build();
    }
}
