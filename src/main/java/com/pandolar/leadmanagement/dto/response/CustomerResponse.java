package com.pandolar.leadmanagement.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record CustomerResponse(
        UUID id,
        String name,
        String email,
        String phone,
        UUID leadId,
        LocalDateTime convertedAt,
        LocalDateTime createdAt
) {}
