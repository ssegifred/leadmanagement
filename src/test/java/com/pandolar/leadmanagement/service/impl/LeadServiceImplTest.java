package com.pandolar.leadmanagement.service.impl;

import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.entity.Customer;
import com.pandolar.leadmanagement.entity.Lead;
import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import com.pandolar.leadmanagement.exception.DuplicateConversionException;
import com.pandolar.leadmanagement.exception.ResourceNotFoundException;
import com.pandolar.leadmanagement.mapper.CustomerMapper;
import com.pandolar.leadmanagement.mapper.LeadMapper;
import com.pandolar.leadmanagement.repository.CustomerRepository;
import com.pandolar.leadmanagement.repository.LeadRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class LeadServiceImplTest {

    @Mock
    private LeadRepository leadRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Spy
    private LeadMapper leadMapper;

    @Spy
    private CustomerMapper customerMapper;

    @InjectMocks
    private LeadServiceImpl leadService;

    private Lead sampleLead;

    @BeforeEach
    void setUp() {
        sampleLead = new Lead();
        sampleLead.setId(UUID.randomUUID());
        sampleLead.setName("John Doe");
        sampleLead.setEmail("john@example.com");
        sampleLead.setPhone("1234567890");
        sampleLead.setStatus(LeadStatus.NEW);
        sampleLead.setCreatedAt(LocalDateTime.now());
        sampleLead.setUpdatedAt(LocalDateTime.now());
    }

    @Test
    @DisplayName("Should create a lead successfully")
    void createLead_success() {
        CreateLeadRequest request = new CreateLeadRequest("John Doe", "john@example.com", "1234567890");
        when(leadRepository.save(any(Lead.class))).thenReturn(sampleLead);

        LeadResponse response = leadService.createLead(request);

        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.status()).isEqualTo(LeadStatus.NEW);

        ArgumentCaptor<Lead> captor = ArgumentCaptor.forClass(Lead.class);
        verify(leadRepository).save(captor.capture());
        assertThat(captor.getValue().getStatus()).isEqualTo(LeadStatus.NEW);
    }

    @Test
    @DisplayName("Should retrieve a lead by ID")
    void getLeadById_success() {
        when(leadRepository.findById(sampleLead.getId())).thenReturn(Optional.of(sampleLead));

        LeadResponse response = leadService.getLeadById(sampleLead.getId());

        assertThat(response.id()).isEqualTo(sampleLead.getId());
        assertThat(response.email()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when lead not found")
    void getLeadById_notFound() {
        UUID id = UUID.randomUUID();
        when(leadRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> leadService.getLeadById(id))
                .isInstanceOf(ResourceNotFoundException.class)
                .hasMessageContaining(id.toString());
    }

    @Test
    @DisplayName("Should retrieve all leads")
    void getAllLeads_success() {
        when(leadRepository.findAll()).thenReturn(List.of(sampleLead));

        List<LeadResponse> responses = leadService.getAllLeads();

        assertThat(responses).hasSize(1);
        assertThat(responses.get(0).email()).isEqualTo("john@example.com");
    }

    @Test
    @DisplayName("Should update a lead successfully")
    void updateLead_success() {
        UpdateLeadRequest request = new UpdateLeadRequest("Jane Doe", "jane@example.com", "0987654321", LeadStatus.CONTACTED);
        Lead updatedLead = new Lead();
        updatedLead.setId(sampleLead.getId());
        updatedLead.setName("Jane Doe");
        updatedLead.setEmail("jane@example.com");
        updatedLead.setPhone("0987654321");
        updatedLead.setStatus(LeadStatus.CONTACTED);
        updatedLead.setCreatedAt(sampleLead.getCreatedAt());
        updatedLead.setUpdatedAt(LocalDateTime.now());

        when(leadRepository.findById(sampleLead.getId())).thenReturn(Optional.of(sampleLead));
        when(leadRepository.save(any(Lead.class))).thenReturn(updatedLead);

        LeadResponse response = leadService.updateLead(sampleLead.getId(), request);

        assertThat(response.name()).isEqualTo("Jane Doe");
        assertThat(response.status()).isEqualTo(LeadStatus.CONTACTED);
    }

    @Test
    @DisplayName("Should reject direct status update to CONVERTED")
    void updateLead_rejectConvertedStatus() {
        UpdateLeadRequest request = new UpdateLeadRequest("John Doe", "john@example.com", null, LeadStatus.CONVERTED);
        when(leadRepository.findById(sampleLead.getId())).thenReturn(Optional.of(sampleLead));

        assertThatThrownBy(() -> leadService.updateLead(sampleLead.getId(), request))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("conversion endpoint");
    }

    @Test
    @DisplayName("Should convert a lead to a customer")
    void convertLead_success() {
        when(leadRepository.findById(sampleLead.getId())).thenReturn(Optional.of(sampleLead));
        when(leadRepository.save(any(Lead.class))).thenReturn(sampleLead);

        Customer customer = new Customer();
        customer.setId(UUID.randomUUID());
        customer.setName(sampleLead.getName());
        customer.setEmail(sampleLead.getEmail());
        customer.setPhone(sampleLead.getPhone());
        customer.setLead(sampleLead);
        customer.setConvertedAt(LocalDateTime.now());
        customer.setCreatedAt(LocalDateTime.now());
        customer.setUpdatedAt(LocalDateTime.now());
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponse response = leadService.convertLead(sampleLead.getId());

        assertThat(response.name()).isEqualTo("John Doe");
        assertThat(response.email()).isEqualTo("john@example.com");
        assertThat(response.leadId()).isEqualTo(sampleLead.getId());
        assertThat(response.convertedAt()).isNotNull();

        verify(leadRepository).save(sampleLead);
        assertThat(sampleLead.getStatus()).isEqualTo(LeadStatus.CONVERTED);
    }

    @Test
    @DisplayName("Should throw DuplicateConversionException when lead already converted")
    void convertLead_alreadyConverted() {
        sampleLead.setStatus(LeadStatus.CONVERTED);
        when(leadRepository.findById(sampleLead.getId())).thenReturn(Optional.of(sampleLead));

        assertThatThrownBy(() -> leadService.convertLead(sampleLead.getId()))
                .isInstanceOf(DuplicateConversionException.class)
                .hasMessageContaining("already been converted");
    }

    @Test
    @DisplayName("Should throw ResourceNotFoundException when converting non-existent lead")
    void convertLead_notFound() {
        UUID id = UUID.randomUUID();
        when(leadRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> leadService.convertLead(id))
                .isInstanceOf(ResourceNotFoundException.class);
    }
}
