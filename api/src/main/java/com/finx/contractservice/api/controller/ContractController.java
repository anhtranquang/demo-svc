package com.finx.contractservice.api.controller;

import com.finx.contractservice.api.dto.*;
import com.finx.contractservice.api.dto.base.ResponseApi;
import com.finx.contractservice.api.mapper.ContractDetailMapper;
import com.finx.contractservice.core.domain.ContractDetail;
import com.finx.contractservice.core.domain.ContractFileMetadata;
import com.finx.contractservice.core.service.ContractFileService;
import com.finx.contractservice.core.service.ContractService;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/internal/contracts")
@AllArgsConstructor
public class ContractController {

    private final ContractService contractService;
    private final ContractFileService contractFileService;
    private final ContractDetailMapper contractDetailMapper;

    @PostMapping("")
    ResponseApi<ContractDetailResponse> requestSignContract(
            @RequestBody @Valid ContractSignRequest contractSignRequest) {
        ContractDetail contractDetail =
                contractService.requestContractSignCloud(
                        contractSignRequest.contractNo(),
                        contractSignRequest.partyId(),
                        contractSignRequest.templateName(),
                        contractSignRequest.data(),
                        contractSignRequest.draftId());

        ContractDetailResponse detailDto = contractDetailMapper.toDto(contractDetail);
        return ResponseApi.success(detailDto);
    }

    @PutMapping("/{contractId}/verify-otp")
    ResponseApi<ContractDetailResponse> verifyOtp(
            @PathVariable Long contractId, @RequestBody @Valid OtpVerifyRequest otpVerifyRequest) {
        ContractDetail contractDetail =
                contractService.authorizeCounterSigningForSignCloud(
                        contractId, otpVerifyRequest.authorizeCode(), otpVerifyRequest.partyId());
        ContractDetailResponse detailDto = contractDetailMapper.toDto(contractDetail);
        return ResponseApi.success(detailDto);
    }

    @PutMapping("/{contractId}/resend-otp")
    ResponseApi<ContractDetailResponse> resendOtp(
            @PathVariable Long contractId, @RequestBody @Valid OtpResendRequest otpResendRequest) {
        ContractDetail contractDetail =
                contractService.regenerateAuthorizeCode(contractId, otpResendRequest.partyId());
        ContractDetailResponse detailDto = contractDetailMapper.toDto(contractDetail);
        return ResponseApi.success(detailDto);
    }

    @PostMapping("/generate")
    ResponseApi<GeneratingContractResponse> generate(
            @RequestBody @Valid GeneratingContractRequest request) {
        ContractFileMetadata metadata =
                ContractFileMetadata.builder()
                        .templateName(request.templateName())
                        .additionData(request.additionData())
                        .data(request.data())
                        .partyId(request.partyId())
                        .build();
        ContractFileMetadata fileMetadata = contractFileService.generate(metadata);
        return ResponseApi.success(
                new GeneratingContractResponse(
                        fileMetadata.getDraftId(), fileMetadata.getTemplateName(), fileMetadata.getUrl()));
    }
}
