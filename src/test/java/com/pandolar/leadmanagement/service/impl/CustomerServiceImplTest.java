package com.pandolar.leadmanagement.service.impl;

import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.entity.Customer;
import com.pandolar.leadmanagement.entity.Lead;
import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import com.pandolar.leadmanagement.exception.ResourceNotFoundException;
import com.pandolar.leadmanagement.mapper.CustomerMapper;
import com.pandolar.leadmanagement.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private CustomerMapper customerMapper;

    @InjectMocks
    private CustomerServiceImpl customerService;

    private Customer sampleCustomer;

    @BeforeEach
    void setUp() {
        Lead lead = new Lead();
        lead.setId(UUID.randomUUID());
        lead.setName("John Doe");
        lead.setEmail("john@example.com");
        lead.setStatus(LeadStatus.CONVERTED);

        sampleCustomer = new Customer();
        sampleCustomer.setId(UUID.randomUUID());
        sampleCustomer.setName("John Doe");
        sampleCustomer.setEmail("john@example.com");
        sampleCustomer.setPhone("1234567890");
        sampleCustomer.setLead(lead);
        sampleCustomer.setConvertedAt(LocalDateTime.now());
        sampleCustomer.setCreatedAt(LocalDateTime.now());
        sampleCustomer.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should retrieve a customer by ID")
    void getCustomerById_success() {
        when(customerRepository.findById(sampleCustomer.getId())).thenReturn(Optional.of(sampleCustomer));

        CustomerResponse response = customerService.getCustomerById(sampleCustomer.getId());

        assertThat(response.id()).isEqualTo(sampleCustomer.getId());
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.leadId()).isEqualTo(sampleCustomer.getLead().getId());
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when customer not found")
    void getCustomerById_notFound() {
        UUID id = UUID.randomUUID();
        when(customerRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> customerService.getCustomerById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Should retrieve all customers")
    void getAllCustomers_success() {
        when(customerRepository.findAll()).thenReturn(List.of(sampleCustomer));

        List<CustomerResponse> responses = customerService.getAllCustomers();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).name()).isEqualTo("John Doe");
    }
}
