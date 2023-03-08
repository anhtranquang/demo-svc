package com.finx.contractservice.infra.client.pdfbox.exception;

public class GenerateFileException extends RuntimeException {
    public GenerateFileException(String error) {
        super(error);
    }
}
