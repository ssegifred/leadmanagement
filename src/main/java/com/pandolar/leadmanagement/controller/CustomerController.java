package com.pandolar.leadmanagement.controller;

import com.pandolar.leadmanagement.constants.ApiPaths;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.ErrorResponse;
import com.pandolar.leadmanagement.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping(ApiPaths.CUSTOMERS)
@Tag(name = "Customer Management", description = "Read-only operations for customers created from lead conversions")
public class CustomerController {

    private static final Logger log = LoggerFactory.getLogger(CustomerController.class);

    private final CustomerService customerService;

    public CustomerController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping
    @Operation(summary = "Retrieve all customers", description = "Returns a list of all customers that were created from lead conversions.")
    @ApiResponse(responseCode = "200", description = "List of customers retrieved successfully")
    public ResponseEntity<List<CustomerResponse>> getAllCustomers() {
        log.info("GET /api/customers - Retrieving all customers");
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping(ApiPaths.ID)
    @Operation(summary = "Retrieve a single customer", description = "Returns a customer by its UUID.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Customer found"),
            @ApiResponse(responseCode = "404", description = "Customer not found",
                    content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
    })
    public ResponseEntity<CustomerResponse> getCustomerById(@PathVariable UUID id) {
        log.info("GET /api/customers/{} - Retrieving customer", id);
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }
}
