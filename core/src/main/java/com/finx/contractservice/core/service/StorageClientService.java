package com.finx.contractservice.core.service;

import com.finx.contractservice.core.domain.DocumentFile;

public interface StorageClientService {
    String saveDraftContract(DocumentFile fileByte);

    String saveSignedContract(DocumentFile fileByte, long partyId);

    String getSignedUrl(String filePath, long duration);
}
