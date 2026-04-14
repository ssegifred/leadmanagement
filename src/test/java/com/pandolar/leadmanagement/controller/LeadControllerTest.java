package com.pandolar.leadmanagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import com.pandolar.leadmanagement.exception.DuplicateConversionException;
import com.pandolar.leadmanagement.exception.GlobalExceptionHandler;
import com.pandolar.leadmanagement.exception.ResourceNotFoundException;
import com.pandolar.leadmanagement.service.LeadService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(LeadController.class)
@Import(GlobalExceptionHandler.class)
class LeadControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private LeadService leadService;

    private final UUID leadId = UUID.randomUUID();
    private final LocalDateTime now = LocalDateTime.now();

    @Test
    @DisplayName("POST /api/leads - should create a lead and return 201")
    void createLead_returnsCreated() throws Exception {
        CreateLeadRequest request = new CreateLeadRequest("John Doe", "john@example.com", "1234567890");
        LeadResponse response = new LeadResponse(leadId, "John Doe", "john@example.com", "1234567890", LeadStatus.NEW, now, now);

        when(leadService.createLead(any(CreateLeadRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/leads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(leadId.toString()))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.email").value("john@example.com"))
                .andExpect(jsonPath("$.status").value("NEW"));
    }

    @Test
    @DisplayName("POST /api/leads - should return 400 for missing email")
    void createLead_missingEmail_returnsBadRequest() throws Exception {
        String body = """
                {"name": "John Doe", "email": "", "phone": "1234567890"}
                """;

        mockMvc.perform(post("/api/leads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    @DisplayName("POST /api/leads - should return 400 for invalid email format")
    void createLead_invalidEmail_returnsBadRequest() throws Exception {
        String body = """
                {"name": "John Doe", "email": "not-an-email", "phone": "1234567890"}
                """;

        mockMvc.perform(post("/api/leads")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(body))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.validationErrors.email").exists());
    }

    @Test
    @DisplayName("GET /api/leads - should return all leads")
    void getAllLeads_returnsOk() throws Exception {
        LeadResponse response = new LeadResponse(leadId, "John Doe", "john@example.com", null, LeadStatus.NEW, now, now);
        when(leadService.getAllLeads()).thenReturn(List.of(response));

        mockMvc.perform(get("/api/leads"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(leadId.toString()));
    }

    @Test
    @DisplayName("GET /api/leads/{id} - should return a single lead")
    void getLeadById_returnsOk() throws Exception {
        LeadResponse response = new LeadResponse(leadId, "John Doe", "john@example.com", null, LeadStatus.NEW, now, now);
        when(leadService.getLeadById(leadId)).thenReturn(response);

        mockMvc.perform(get("/api/leads/{id}", leadId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("John Doe"));
    }

    @Test
    @DisplayName("GET /api/leads/{id} - should return 404 for non-existent lead")
    void getLeadById_notFound() throws Exception {
        when(leadService.getLeadById(leadId)).thenThrow(new ResourceNotFoundException("Lead not found with id: " + leadId));

        mockMvc.perform(get("/api/leads/{id}", leadId))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Lead not found with id: " + leadId));
    }

    @Test
    @DisplayName("PUT /api/leads/{id} - should update a lead")
    void updateLead_returnsOk() throws Exception {
        UpdateLeadRequest request = new UpdateLeadRequest("Jane Doe", "jane@example.com", null, LeadStatus.CONTACTED);
        LeadResponse response = new LeadResponse(leadId, "Jane Doe", "jane@example.com", null, LeadStatus.CONTACTED, now, now);

        when(leadService.updateLead(eq(leadId), any(UpdateLeadRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/leads/{id}", leadId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Jane Doe"))
                .andExpect(jsonPath("$.status").value("CONTACTED"));
    }

    @Test
    @DisplayName("POST /api/leads/{id}/convert - should convert lead and return 201")
    void convertLead_returnsCreated() throws Exception {
        CustomerResponse response = new CustomerResponse(UUID.randomUUID(), "John Doe", "john@example.com", "1234567890", leadId, now, now);
        when(leadService.convertLead(leadId)).thenReturn(response);

        mockMvc.perform(post("/api/leads/{id}/convert", leadId))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.leadId").value(leadId.toString()))
                .andExpect(jsonPath("$.convertedAt").exists());
    }

    @Test
    @DisplayName("POST /api/leads/{id}/convert - should return 409 for duplicate conversion")
    void convertLead_duplicate_returnsConflict() throws Exception {
        when(leadService.convertLead(leadId)).thenThrow(new DuplicateConversionException("Lead with id " + leadId + " has already been converted"));

        mockMvc.perform(post("/api/leads/{id}/convert", leadId))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Lead with id " + leadId + " has already been converted"));
    }
}
