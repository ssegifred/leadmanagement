package com.pandolar.leadmanagement.mapper;

import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.entity.Lead;
import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import com.pandolar.leadmanagement.util.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class LeadMapper {

    public Lead toEntity(CreateLeadRequest request) {
        return Lead.builder()
                .name(StringUtils.trimOrNull(request.name()))
                .email(StringUtils.normalizeEmail(request.email()))
                .phone(StringUtils.trimOrNull(request.phone()))
                .status(LeadStatus.NEW)
                .build();
    }

    public void updateEntity(Lead lead, UpdateLeadRequest request) {
        lead.setName(StringUtils.trimOrNull(request.name()));
        lead.setEmail(StringUtils.normalizeEmail(request.email()));
        lead.setPhone(StringUtils.trimOrNull(request.phone()));

        if (request.status() != null) {
            lead.setStatus(request.status());
        }
    }

    public LeadResponse toResponse(Lead lead) {
        return new LeadResponse(
                lead.getId(),
                lead.getName(),
                lead.getEmail(),
                lead.getPhone(),
                lead.getStatus(),
                lead.getCreatedAt(),
                lead.getUpdatedAt()
        );
    }
}
