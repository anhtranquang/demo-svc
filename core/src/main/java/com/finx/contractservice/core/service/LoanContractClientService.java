package com.finx.contractservice.core.service;

import com.finx.contractservice.core.domain.ContractCertificate;
import com.finx.contractservice.core.domain.ContractDetail;
import com.finx.contractservice.core.domain.ContractParty;
import com.finx.contractservice.core.domain.DocumentFile;

public interface LoanContractClientService {

    /**
     * generateCertificateForSignCloud
     *
     * @param partyDetail {@link ContractParty}
     * @return Certificate of contract
     */
    ContractCertificate generateCertificateForSignCloud(String contractNo, ContractParty partyDetail);

    /**
     * Send contract file to sign Cloud
     *
     * @param contract ContractData
     * @param contractFileUrl Contract filePath
     * @return billCode from SignCloud
     */
    String sendContractForSignCloud(ContractDetail contract, DocumentFile contractFileUrl);

    /**
     * authorizeCounterSigningForSignCloud
     *
     * @param agreementId
     * @param authorizeCode received from OTP
     * @param billCode Received from sendContractForSignCloud
     * @param partyId Received from sendContractForSignCloud
     * @return Certificate of contract
     */
    DocumentFile authorizeCounterSigningForSignCloud(
            String agreementId, String authorizeCode, String billCode, Long partyId);

    /**
     * regenerateAuthorizationCodeForSignCloud
     *
     * @param agreementId
     */
    void regenerateAuthorizationCodeForSignCloud(String agreementId, Long partyId);

    /**
     * getCertificateDetailForSignCloud
     *
     * @param agreementId
     */
    ContractCertificate getCertificateDetailForSignCloud(String agreementId);

    void validateBlockingUser(Long partyId);
}
