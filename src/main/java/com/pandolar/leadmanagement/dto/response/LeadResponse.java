package com.pandolar.leadmanagement.dto.response;

import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.LocalDateTime;
import java.util.UUID;

@Schema(description = "Lead details")
public record LeadResponse(

        @Schema(description = "Unique identifier", example = "f47ac10b-58cc-4372-a567-0e02b2c3d479")
        UUID id,

        @Schema(description = "Full name", example = "John Doe")
        String name,

        @Schema(description = "Email address", example = "john@example.com")
        String email,

        @Schema(description = "Phone number", example = "+254712345678")
        String phone,

        @Schema(description = "Current lead status", example = "NEW")
        LeadStatus status,

        @Schema(description = "Record creation timestamp")
        LocalDateTime createdAt,

        @Schema(description = "Last update timestamp")
        LocalDateTime updatedAt
) {}
