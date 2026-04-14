package com.pandolar.leadmanagement.dto.response;

import com.pandolar.leadmanagement.entity.enums.LeadStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record LeadResponse(
        UUID id,
        String name,
        String email,
        String phone,
        LeadStatus status,
        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {}
