package com.finx.contractservice.core.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class DocumentFile {

    public DocumentFile(byte[] file, String fileName) {
        this.file = file;
        this.fileName = fileName;
    }

    private byte[] file;
    private String fileName;
    private ContractTemplate contractTemplate;
}
