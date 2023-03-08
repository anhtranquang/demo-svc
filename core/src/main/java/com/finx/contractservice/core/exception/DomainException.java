package com.finx.contractservice.core.exception;

public class DomainException extends RuntimeException {

    private final DomainCode domainCode;

    public DomainException(DomainCode domainCode, Object... args) {
        super(String.format(domainCode.getMessage(), args));
        this.domainCode = domainCode;
    }

    public DomainCode getDomainCode() {
        return domainCode;
    }
}
