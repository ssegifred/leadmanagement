package com.pandolar.leadmanagement.service;

import com.pandolar.leadmanagement.dto.response.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

    CustomerResponse getCustomerById(UUID id);

    List<CustomerResponse> getAllCustomers();
}
