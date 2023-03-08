package com.finx.contractservice.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class JsonUtil {
    private static ObjectMapper objectMapper;

    @Autowired
    private JsonUtil(ObjectMapper objectMapper) {
        JsonUtil.objectMapper = objectMapper;
    }

    public static ObjectMapper getInstance() {
        return objectMapper;
    }
}
