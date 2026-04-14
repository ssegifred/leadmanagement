package com.pandolar.leadmanagement.service;

import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.LeadResponse;

import java.util.List;
import java.util.UUID;

public interface LeadService {

    LeadResponse createLead(CreateLeadRequest request);

    LeadResponse updateLead(UUID id, UpdateLeadRequest request);

    LeadResponse getLeadById(UUID id);

    List<LeadResponse> getAllLeads();

    CustomerResponse convertLead(UUID leadId);
}
