package com.finx.contractservice.infra.client.s3.exception;

public class FileUploadException extends RuntimeException {

    public FileUploadException() {
        super("Error while upload file");
    }
}
