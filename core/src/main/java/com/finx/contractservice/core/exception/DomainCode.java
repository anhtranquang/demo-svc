package com.finx.contractservice.core.exception;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public enum DomainCode {
    UNKNOWN_ERROR("000", "Unknown error"),
    INVALID_INPUT("001", "Invalid input"),
    NUMBER_OF_ALLOWED_OTP_GENERATION_EXCEEDED("002", "Number of allowed OTP Generation exceeded"),
    INVALID_OTP("003", "Invalid OTP"),
    OTP_TIMEOUT("004", "OTP Timeout"),
    INVALID_OTP_MANY_TIME("005", "OTP is invalid many times");

    private final String value;

    private final String message;

    private static final Map<String, DomainCode> ENUM_MAP;

    static {
        final Map<String, DomainCode> map = new HashMap<>();
        for (DomainCode instance : DomainCode.values()) {
            map.put(instance.getValue(), instance);
        }
        ENUM_MAP = Collections.unmodifiableMap(map);
    }

    DomainCode(String value, String message) {
        this.value = value;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public String getValue() {
        return value;
    }

    public static DomainCode get(String value) {
        return ENUM_MAP.get(value);
    }
}
