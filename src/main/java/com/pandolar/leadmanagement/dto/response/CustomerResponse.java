package com.pandolar.leadmanagement.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Customer details (created from lead conversion)")
public record CustomerResponse(

        @Schema(description = "Unique customer identifier", example = "a1b2c3d4-e5f6-7890-abcd-ef1234567890")
        UUID id,

        @Schema(description = "Full name (copied from lead)", example = "John Doe")
        String name,

        @Schema(description = "Email address (copied from lead)", example = "john@example.com")
        String email,

        @Schema(description = "Phone number (copied from lead)", example = "+254712345678")
        String phone,

        @Schema(description = "ID of the original lead that was converted", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID leadId,

        @Schema(description = "Timestamp when the lead was converted to a customer")
        LocalDateTime convertedAt,

        @Schema(description = "Record creation timestamp")
        LocalDateTime createdAt
) {}
