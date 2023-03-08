package com.finx.contractservice.infra.client.s3;

import com.amazonaws.HttpMethod;
import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.GeneratePresignedUrlRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.finx.contractservice.core.enumeration.UploadStatus;
import com.finx.contractservice.infra.client.s3.exception.DocumentFetchException;
import java.io.ByteArrayInputStream;
import java.net.URL;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class AWSS3Client {

    private final String bucketName;
    private final AmazonS3 amazonS3;

    public AWSS3Client(@Value("${awsS3.bucketName}") String bucketName, AmazonS3 amazonS3) {
        this.bucketName = bucketName;
        this.amazonS3 = amazonS3;
    }

    public UploadStatus putObject(String filePath, byte[] file) {

        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentType(MediaType.MULTIPART_FORM_DATA.toString());
        objectMetadata.setContentLength(file.length);
        try {
            amazonS3.putObject(bucketName, filePath, new ByteArrayInputStream(file), objectMetadata);
            return UploadStatus.SUCCESS;
        } catch (SdkClientException e) {
            log.error("Error while uploading document on DMS", e);
            return UploadStatus.FAILED;
        }
    }

    public String getPreSignedUrl(String fileName, long duration) {
        try {
            log.debug("Generating pre-signed URL for [bucket={}] and [file={}]", bucketName, fileName);
            GeneratePresignedUrlRequest generatePresignedUrlRequest =
                    new GeneratePresignedUrlRequest(bucketName, fileName);
            generatePresignedUrlRequest.setMethod(HttpMethod.GET);
            generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis() + duration));

            URL url = amazonS3.generatePresignedUrl(generatePresignedUrlRequest);
            return url.toString();
        } catch (SdkClientException ex) {
            throw new DocumentFetchException(ex);
        }
    }
}
