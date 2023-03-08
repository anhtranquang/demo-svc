package com.finx.contractservice.infra.repository.mapper;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.finx.contractservice.common.utils.JsonUtil;
import com.finx.contractservice.core.domain.ContractFileMetadata;
import com.finx.contractservice.infra.repository.entity.ContractDraftEntity;
import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ContractDraftEntityMapper {

    default Map<String, String> getData(JsonNode contractData) {
        if (Objects.isNull(contractData)) {
            return Collections.emptyMap();
        }
        return JsonUtil.getInstance().convertValue(contractData, new TypeReference<>() {});
    }

    default JsonNode getNode(Map<String, String> contractData) {
        if (Objects.isNull(contractData)) {
            return null;
        }
        return JsonUtil.getInstance().convertValue(contractData, new TypeReference<>() {});
    }

    @Mapping(target = "data", expression = "java(getData(entity.getData()))")
    @Mapping(target = "additionData", expression = "java(getData(entity.getAdditionData()))")
    @Mapping(target = "url", ignore = true)
    @Mapping(target = "path", source = "url")
    @Mapping(target = "draftId", source = "id")
    ContractFileMetadata toModel(ContractDraftEntity entity);

    @Mapping(target = "data", expression = "java(getNode(entity.getData()))")
    @Mapping(target = "additionData", expression = "java(getNode(entity.getAdditionData()))")
    @Mapping(target = "url", source = "path")
    @Mapping(target = "id", source = "draftId")
    ContractDraftEntity toEntity(ContractFileMetadata entity);
}
