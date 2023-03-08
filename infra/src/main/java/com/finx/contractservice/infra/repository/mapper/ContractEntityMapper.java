package com.finx.contractservice.infra.repository.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.finx.contractservice.common.utils.JsonUtil;
import com.finx.contractservice.core.domain.ContractCertificate;
import com.finx.contractservice.core.domain.ContractDetail;
import com.finx.contractservice.core.domain.ContractParty;
import com.finx.contractservice.core.domain.ContractTemplate;
import com.finx.contractservice.infra.repository.entity.ContractCertificateEntity;
import com.finx.contractservice.infra.repository.entity.ContractEntity;
import com.finx.contractservice.infra.repository.entity.TemplateEntity;
import java.util.*;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractEntityMapper {

    @Mapping(target = "contractId", source = "id")
    @Mapping(target = "data", expression = "java(getData(entity.getData()))")
    @Mapping(target = "partyDetail", expression = "java(getPartyDetail(entity.getPartyDetail()))")
    @Mapping(
            target = "certificate",
            expression = "java(getContractCertificate(entity.getCertificate()))")
    @Mapping(target = "issuer", source = "createdBy")
    @Mapping(target = "template", source = "template")
    ContractDetail toModel(ContractEntity entity);

    default Map<String, String> getData(JsonNode contractData) {
        if (Objects.isNull(contractData)) {
            return Collections.emptyMap();
        }
        return JsonUtil.getInstance().convertValue(contractData, new TypeReference<>() {});
    }

    default ContractParty getPartyDetail(JsonNode partyDetail) {
        if (Objects.isNull(partyDetail)) {
            return null;
        }
        return JsonUtil.getInstance().convertValue(partyDetail, ContractParty.class);
    }

    default ContractCertificate getContractCertificate(ContractCertificateEntity certificate) {
        if (Objects.isNull(certificate)) {
            return null;
        }

        return this.toCertificateModal(certificate);
    }

    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "updatedBy", ignore = true)
    ContractCertificateEntity toCertificateEntity(ContractCertificate certificate);

    ContractCertificate toCertificateModal(ContractCertificateEntity certificate);

    @Mapping(target = "metadata", expression = "java(getData(template.getMetadata()))")
    ContractTemplate toTemplateModel(TemplateEntity template);
}
