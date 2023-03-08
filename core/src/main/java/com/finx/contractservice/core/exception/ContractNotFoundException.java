package com.finx.contractservice.core.exception;

public class ContractNotFoundException extends RuntimeException {
    public ContractNotFoundException(String contractId) {
        super(contractId + " is not found.");
    }

    public ContractNotFoundException(Long contractId) {
        super(contractId + " is not found.");
    }
}
