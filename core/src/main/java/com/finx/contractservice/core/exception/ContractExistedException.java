package com.finx.contractservice.core.exception;

public class ContractExistedException extends RuntimeException {
    public ContractExistedException(Long contractId) {
        super("[ContractId = " + contractId + "] is found.");
    }

    public ContractExistedException(String message) {
        super(message);
    }
}
