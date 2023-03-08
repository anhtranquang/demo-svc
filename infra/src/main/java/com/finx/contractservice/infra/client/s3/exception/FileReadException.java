package com.finx.contractservice.infra.client.s3.exception;

public class FileReadException extends RuntimeException {

    public FileReadException() {
        super("Error while reading file");
    }
}
