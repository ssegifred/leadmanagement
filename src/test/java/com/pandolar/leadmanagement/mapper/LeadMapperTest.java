package com.pandolar.leadmanagement.mapper;

import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.entity.Lead;
import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

class LeadMapperTest {

    private final LeadMapper leadMapper = new LeadMapper();

    @Test
    @DisplayName("Should map CreateLeadRequest to Lead entity with sanitized inputs")
    void toEntity_sanitizesInputs() {
        CreateLeadRequest request = new CreateLeadRequest("  John Doe  ", "  JOHN@EXAMPLE.COM  ", "  123  ");

        Lead lead = leadMapper.toEntity(request);

        assertThat(lead.getName()).isEqualTo("John Doe");
        assertThat(lead.getEmail()).isEqualTo("john@example.com");
        assertThat(lead.getPhone()).isEqualTo("123");
        assertThat(lead.getStatus()).isEqualTo(LeadStatus.NEW);
    }

    @Test
    @DisplayName("Should handle null phone in CreateLeadRequest")
    void toEntity_nullPhone() {
        CreateLeadRequest request = new CreateLeadRequest("John Doe", "john@example.com", null);

        Lead lead = leadMapper.toEntity(request);

        assertThat(lead.getPhone()).isNull();
    }

    @Test
    @DisplayName("Should update existing Lead entity from UpdateLeadRequest")
    void updateEntity_appliesChanges() {
        Lead lead = new Lead();
        lead.setName("Old Name");
        lead.setEmail("old@example.com");
        lead.setStatus(LeadStatus.NEW);

        UpdateLeadRequest request = new UpdateLeadRequest("  New Name  ", "  NEW@EXAMPLE.COM  ", null, LeadStatus.CONTACTED);

        leadMapper.updateEntity(lead, request);

        assertThat(lead.getName()).isEqualTo("New Name");
        assertThat(lead.getEmail()).isEqualTo("new@example.com");
        assertThat(lead.getPhone()).isNull();
        assertThat(lead.getStatus()).isEqualTo(LeadStatus.CONTACTED);
    }

    @Test
    @DisplayName("Should not change status when UpdateLeadRequest status is null")
    void updateEntity_preservesStatusWhenNull() {
        Lead lead = new Lead();
        lead.setStatus(LeadStatus.QUALIFIED);

        UpdateLeadRequest request = new UpdateLeadRequest("Name", "email@example.com", null, null);

        leadMapper.updateEntity(lead, request);

        assertThat(lead.getStatus()).isEqualTo(LeadStatus.QUALIFIED);
    }

    @Test
    @DisplayName("Should map Lead entity to LeadResponse DTO")
    void toResponse_mapsAllFields() {
        Lead lead = new Lead();
        UUID id = UUID.randomUUID();
        LocalDateTime now = LocalDateTime.now();
        lead.setId(id);
        lead.setName("John Doe");
        lead.setEmail("john@example.com");
        lead.setPhone("123");
        lead.setStatus(LeadStatus.NEW);
        lead.setCreatedAt(now);
        lead.setUpdatedAt(now);

        LeadResponse response = leadMapper.toResponse(lead);

        assertThat(response.id()).isEqualTo(id);
        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.phone()).isEqualTo("123");
        assertThat(response.status()).isEqualTo(LeadStatus.NEW);
        assertThat(response.createdAt()).isEqualTo(now);
        assertThat(response.updatedAt()).isEqualTo(now);
    }
}
