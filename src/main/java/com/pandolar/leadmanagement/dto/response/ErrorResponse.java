package com.pandolar.leadmanagement.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.Map;

@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Error response returned for all API errors")
public record ErrorResponse(

        @Schema(description = "Timestamp of the error")
        LocalDateTime timestamp,

        @Schema(description = "HTTP status code", example = "400")
        int status,

        @Schema(description = "HTTP status reason", example = "Bad Request")
        String error,

        @Schema(description = "Error message describing what went wrong", example = "Validation failed")
        String message,

        @Schema(description = "Request path that caused the error", example = "/api/leads")
        String path,

        @Schema(description = "Field-level validation errors (present only for validation failures)")
        Map<String, String> validationErrors
) {

    public ErrorResponse(int status, String error, String message, String path) {
        this(LocalDateTime.now(), status, error, message, path, null);
    }

    public ErrorResponse(int status, String error, String message, String path, Map<String, String> validationErrors) {
        this(LocalDateTime.now(), status, error, message, path, validationErrors);
    }
}
