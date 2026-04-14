package com.pandolar.leadmanagement.controller;

import com.pandolar.leadmanagement.constants.ApiPaths;
import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.service.LeadService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.LEADS)
public class LeadController {

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request) {
        LeadResponse response = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    public ResponseEntity<List<LeadResponse>> getAllLeads() {
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    @GetMapping(ApiPaths.ID)
    public ResponseEntity<LeadResponse> getLeadById(@PathVariable UUID id) {
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    @PutMapping(ApiPaths.ID)
    public ResponseEntity<LeadResponse> updateLead(@PathVariable UUID id, @Valid @RequestBody UpdateLeadRequest request) {
        return ResponseEntity.ok(leadService.updateLead(id, request));
    }

    @PostMapping(ApiPaths.LEAD_CONVERT)
    public ResponseEntity<CustomerResponse> convertLead(@PathVariable UUID id) {
        CustomerResponse response = leadService.convertLead(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
