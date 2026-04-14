package com.pandolar.leadmanagement.controller;

import com.pandolar.leadmanagement.constants.ApiPaths;
import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.ErrorResponse;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.service.LeadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
@Tag(name = "Lead Management", description = "CRUD operations for leads and lead-to-customer conversion")
public class LeadController {

    private static final Logger log = LoggerFactory.getLogger(LeadController.class);

    private final LeadService leadService;

    public LeadController(LeadService leadService) {
        this.leadService = leadService;
    }

    @PostMapping
    @Operation(summary = "Create a new lead", description = "Creates a new lead with status NEW. Email must be unique.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lead created successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed (missing or invalid fields)",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "A lead with this email already exists",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LeadResponse> createLead(@Valid @RequestBody CreateLeadRequest request) {
        log.info("POST /api/leads - Creating lead with email: {}", request.email());
        LeadResponse response = leadService.createLead(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping
    @Operation(summary = "Retrieve all leads", description = "Returns a list of all leads in the system.")
    @ApiResponse(responseCode = "200", description = "List of leads retrieved successfully")
    public ResponseEntity<List<LeadResponse>> getAllLeads() {
        log.info("GET /api/leads - Retrieving all leads");
        return ResponseEntity.ok(leadService.getAllLeads());
    }

    @GetMapping(ApiPaths.ID)
    @Operation(summary = "Retrieve a single lead", description = "Returns a lead by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lead found"),
            @ApiResponse(responseCode = "404", description = "Lead not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LeadResponse> getLeadById(@PathVariable UUID id) {
        log.info("GET /api/leads/{} - Retrieving lead", id);
        return ResponseEntity.ok(leadService.getLeadById(id));
    }

    @PutMapping(ApiPaths.ID)
    @Operation(summary = "Update a lead", description = "Updates an existing lead. Cannot set status to CONVERTED directly — use the conversion endpoint instead.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lead updated successfully"),
            @ApiResponse(responseCode = "400", description = "Validation failed or attempted to set CONVERTED status",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "404", description = "Lead not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Email conflict with existing lead",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<LeadResponse> updateLead(@PathVariable UUID id, @Valid @RequestBody UpdateLeadRequest request) {
        log.info("PUT /api/leads/{} - Updating lead", id);
        return ResponseEntity.ok(leadService.updateLead(id, request));
    }

    @PostMapping(ApiPaths.LEAD_CONVERT)
    @Operation(summary = "Convert a lead to a customer", description = "Converts a lead into a customer. Creates a new customer record, sets lead status to CONVERTED, and records the conversion timestamp. A lead can only be converted once.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Lead converted to customer successfully"),
            @ApiResponse(responseCode = "404", description = "Lead not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
            @ApiResponse(responseCode = "409", description = "Lead has already been converted",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CustomerResponse> convertLead(@PathVariable UUID id) {
        log.info("POST /api/leads/{}/convert - Converting lead to customer", id);
        CustomerResponse response = leadService.convertLead(id);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
}
