package com.finx.contractservice.common.utils;

import java.io.IOException;
import java.io.InputStream;
import org.apache.commons.io.IOUtils;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;

public class FileUtil {

    public static byte[] getByteArray(String path) {
        Resource resource = new ClassPathResource(path);

        byte[] backSideOfIDDocument;
        try {
            backSideOfIDDocument = IOUtils.toByteArray(resource.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return backSideOfIDDocument;
    }

    public static byte[] getByteArrayFromInputStream(InputStream inputStream) {

        byte[] document;
        try {
            document = IOUtils.toByteArray(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return document;
    }
}
