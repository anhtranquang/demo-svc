package com.finx.contractservice.infra.service;

import com.finx.contractservice.core.domain.DocumentFile;
import com.finx.contractservice.core.enumeration.UploadStatus;
import com.finx.contractservice.core.service.StorageClientService;
import com.finx.contractservice.infra.client.s3.AWSS3Client;
import com.finx.contractservice.infra.client.s3.exception.FileUploadException;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class S3ClientService implements StorageClientService {

    private final AWSS3Client awss3Client;
    private final String draftContractPath;
    private final String signedContractPath;

    public S3ClientService(
            AWSS3Client awss3Client,
            @Value("${awsS3.filePathPrefix.DRAFT_CONTRACT}") String draftContractPath,
            @Value("${awsS3.filePathPrefix.SIGNED_CONTRACT}") String signedContractPath) {
        this.awss3Client = awss3Client;
        this.draftContractPath = draftContractPath;
        this.signedContractPath = signedContractPath;
    }

    @Override
    public String saveDraftContract(DocumentFile fileByte) {
        String prefixPath = DateTimeFormatter.ofPattern("yyyy/MM/dd").format(ZonedDateTime.now());
        String path = "%s/%s/%s".formatted(draftContractPath, prefixPath, fileByte.getFileName());
        UploadStatus status = awss3Client.putObject(path, fileByte.getFile());
        if (UploadStatus.FAILED.equals(status)) {
            throw new FileUploadException();
        }
        return path;
    }

    @Override
    public String saveSignedContract(DocumentFile fileByte, long partyId) {
        String path = "%s/%s/%s".formatted(signedContractPath, partyId, fileByte.getFileName());
        UploadStatus status = awss3Client.putObject(path, fileByte.getFile());
        if (UploadStatus.FAILED.equals(status)) {
            throw new FileUploadException();
        }
        return path;
    }

    @Override
    public String getSignedUrl(String filePath, long duration) {
        return awss3Client.getPreSignedUrl(filePath, duration);
    }
}
