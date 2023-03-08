package com.finx.contractservice.infra.event;

import com.finx.contractservice.core.domain.ContractCertificate;
import com.finx.contractservice.infra.event.dto.ContractSignedEvent;
import com.finx.contractservice.infra.repository.dao.CertificateDao;
import com.finx.contractservice.infra.repository.entity.ContractCertificateEntity;
import com.finx.contractservice.infra.service.FptEsignClientService;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class ContractEventListener {

    private final FptEsignClientService fptEsignClientService;
    private final CertificateDao certificateDao;

    public ContractEventListener(
            FptEsignClientService fptEsignClientService, CertificateDao certificateDao) {
        this.fptEsignClientService = fptEsignClientService;
        this.certificateDao = certificateDao;
    }

    @Async(value = "eventTaskPoolExecutor")
    @EventListener
    void handleSignedContractEvent(ContractSignedEvent event) {
        log.info("### Handle Signed Contract Event [AgreementId={}] ###", event.getAgreementId());
        ContractCertificate certificate =
                fptEsignClientService.getCertificateDetailForSignCloud(event.getAgreementId());

        Optional<ContractCertificateEntity> entity =
                certificateDao.findByAgreementId(event.getAgreementId());

        entity.ifPresent(
                i -> {
                    i.setValidTo(certificate.getValidTo());
                    i.setValidFrom(certificate.getValidFrom());
                    i.setCertificate(certificate.getCertificate());
                    i.setCertificateDn(certificate.getCertificateDn());
                    i.setCertificateSerialNumber(certificate.getCertificateSerialNumber());
                    certificateDao.save(i);
                });
    }
}
