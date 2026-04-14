package com.pandolar.leadmanagement.dto.request;

import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for updating an existing lead")
public record UpdateLeadRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
        @Schema(description = "Full name of the lead", example = "Jane Doe")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Schema(description = "Email address (must be unique)", example = "jane@example.com")
        String email,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        @Schema(description = "Phone number (optional)", example = "+256712345678")
        String phone,

        @Schema(description = "Lead status. Cannot be set to CONVERTED directly — use the conversion endpoint.",
                example = "CONTACTED", allowableValues = {"NEW", "CONTACTED", "QUALIFIED"})
        LeadStatus status
) {}
