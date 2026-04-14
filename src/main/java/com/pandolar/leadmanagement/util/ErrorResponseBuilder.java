package com.pandolar.leadmanagement.util;

import com.pandolar.leadmanagement.dto.response.ErrorResponse;
import org.springframework.http.HttpStatus;

import java.util.Map;

public final class ErrorResponseBuilder {

    private ErrorResponseBuilder() {
    }

    public static ErrorResponse build(HttpStatus status, String message, String path) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
    }

    public static ErrorResponse buildWithValidationErrors(HttpStatus status, String message, String path, Map<String, String> validationErrors) {
        return new ErrorResponse(
                status.value(),
                status.getReasonPhrase(),
                message,
                path,
                validationErrors
        );
    }
}
