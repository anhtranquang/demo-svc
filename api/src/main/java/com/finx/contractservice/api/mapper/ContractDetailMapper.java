package com.finx.contractservice.api.mapper;

import com.finx.contractservice.api.dto.ContractDetailResponse;
import com.finx.contractservice.core.domain.ContractDetail;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface ContractDetailMapper {
    ContractDetailResponse toDto(ContractDetail contractDetail);
}
