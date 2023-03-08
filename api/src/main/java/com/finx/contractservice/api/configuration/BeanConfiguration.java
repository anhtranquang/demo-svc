package com.finx.contractservice.api.configuration;

import com.finx.contractservice.core.service.ContractFileService;
import com.finx.contractservice.core.service.ContractService;
import com.finx.contractservice.core.service.LoanContractClientService;
import com.finx.contractservice.infra.repository.ContractCertificateRepositoryImpl;
import com.finx.contractservice.infra.repository.ContractFileRepositoryImpl;
import com.finx.contractservice.infra.repository.ContractRepositoryImpl;
import com.finx.contractservice.infra.service.FptEsignClientService;
import com.finx.contractservice.infra.service.PartyClientServiceImpl;
import com.finx.contractservice.infra.service.S3ClientService;
import com.finx.contractservice.infra.service.TemplateServiceImpl;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanConfiguration {

    @Bean
    public ContractService contractService(
            FptEsignClientService fptEsignClientService,
            ContractRepositoryImpl contractRepository,
            PartyClientServiceImpl partyClientService,
            ContractCertificateRepositoryImpl contractCertificateRepository,
            TemplateServiceImpl templateService,
            S3ClientService s3ClientService) {
        return new ContractService(
                fptEsignClientService,
                contractRepository,
                partyClientService,
                templateService,
                s3ClientService,
                contractCertificateRepository);
    }

    @Bean
    ContractFileService contractFileService(
            ContractFileRepositoryImpl contractFileRepository,
            TemplateServiceImpl templateService,
            S3ClientService s3ClientService,
            LoanContractClientService contractClientService) {
        return new ContractFileService(
                contractFileRepository, templateService, s3ClientService, contractClientService);
    }
}
