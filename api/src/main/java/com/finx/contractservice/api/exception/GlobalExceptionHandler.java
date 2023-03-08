package com.finx.contractservice.api.exception;

import com.finx.contractservice.api.dto.base.ResponseApi;
import com.finx.contractservice.core.exception.DomainException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(DomainException.class)
    public ResponseApi<Object> handleDomainException(HttpServletRequest request, DomainException e) {
        return ResponseApi.error(e.getDomainCode().getValue(), e.getMessage());
    }
}
