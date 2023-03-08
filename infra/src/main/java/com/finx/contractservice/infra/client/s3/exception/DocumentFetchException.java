package com.finx.contractservice.infra.client.s3.exception;

public class DocumentFetchException extends RuntimeException {

    public DocumentFetchException(Exception ex) {
        super(ex);
    }
}
