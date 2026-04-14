package com.pandolar.leadmanagement.service.impl;

import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.entity.Customer;
import com.pandolar.leadmanagement.constants.ErrorMessages;
import com.pandolar.leadmanagement.exception.ResourceNotFoundException;
import com.pandolar.leadmanagement.mapper.CustomerMapper;
import com.pandolar.leadmanagement.repository.CustomerRepository;
import com.pandolar.leadmanagement.service.CustomerService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    public CustomerServiceImpl(CustomerRepository customerRepository, CustomerMapper customerMapper) {
        this.customerRepository = customerRepository;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional(readOnly = true)
    public CustomerResponse getCustomerById(UUID id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.CUSTOMER_NOT_FOUND + id));
        return customerMapper.toResponse(customer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<CustomerResponse> getAllCustomers() {
        return customerRepository.findAll().stream()
                .map(customerMapper::toResponse)
                .toList();
    }
}
