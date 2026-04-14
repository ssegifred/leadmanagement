package com.pandolar.leadmanagement.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Request body for creating a new lead")
public record CreateLeadRequest(

        @NotBlank(message = "Name is required")
        @Size(max = 255, message = "Name must not exceed 255 characters")
        @Schema(description = "Full name of the lead", example = "John Doe")
        String name,

        @NotBlank(message = "Email is required")
        @Email(message = "Email must be a valid email address")
        @Schema(description = "Email address (must be unique)", example = "john@example.com")
        String email,

        @Size(max = 20, message = "Phone must not exceed 20 characters")
        @Schema(description = "Phone number (optional)", example = "+254712345678")
        String phone
) {}
