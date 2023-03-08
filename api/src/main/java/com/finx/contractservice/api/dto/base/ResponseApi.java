package com.finx.contractservice.api.dto.base;

import java.util.Collections;
import java.util.List;
import org.slf4j.MDC;

public record ResponseApi<T>(ResponseStatus status, T payload, ResponseMeta meta) {

    private static final String SUCCESS = "SUCCESS";

    private ResponseApi(ResponseStatus status, ResponseMeta meta) {
        this(status, null, meta);
    }

    public static <T> ResponseApi<T> success(ResponseStatus status, T payload) {
        ResponseMeta meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        return success(status, payload, meta);
    }

    public static <T> ResponseApi<T> success(T payload) {
        return success(new ResponseStatus(SUCCESS), payload);
    }

    public static <T> ResponseApi<T> success(T payload, ResponseMeta meta) {
        return new ResponseApi<>(new ResponseStatus(SUCCESS), payload, meta);
    }

    public static <T> ResponseApi<T> success(ResponseStatus status, T payload, ResponseMeta meta) {
        if (meta == null) {
            meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        }
        return new ResponseApi<>(status, payload, meta);
    }

    public static <T> ResponseApi<T> success(T payload, String nextCursor) {
        var meta = new ResponseMeta(MDC.get("X-Request-ID"), nextCursor);
        return new ResponseApi<>(new ResponseStatus(SUCCESS), payload, meta);
    }

    public static <T> ResponseApi<T> error(String errorCode, String errorMessage) {
        return error(errorCode, errorMessage, Collections.emptyList());
    }

    public static <T> ResponseApi<T> error(
            String errorCode, String errorMessage, List<FieldError> fieldErrors) {
        ResponseStatus responseStatus = new ResponseStatus(errorCode, errorMessage, fieldErrors);
        ResponseMeta meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        return new ResponseApi<>(responseStatus, meta);
    }

    public static <T> ResponseApi<T> error(String errorCode, String errorMessage, T payload) {
        ResponseStatus responseStatus = new ResponseStatus(errorCode, errorMessage, null);
        ResponseMeta meta = ResponseMeta.fromRequestId(MDC.get("X-Request-ID"));
        return new ResponseApi<>(responseStatus, payload, meta);
    }
}
