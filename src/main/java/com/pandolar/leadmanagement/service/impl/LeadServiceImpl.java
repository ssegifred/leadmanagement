package com.pandolar.leadmanagement.service.impl;

import com.pandolar.leadmanagement.dto.request.CreateLeadRequest;
import com.pandolar.leadmanagement.dto.request.UpdateLeadRequest;
import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.dto.response.LeadResponse;
import com.pandolar.leadmanagement.entity.Customer;
import com.pandolar.leadmanagement.entity.Lead;
import com.pandolar.leadmanagement.entity.enums.LeadStatus;
import com.pandolar.leadmanagement.constants.ErrorMessages;
import com.pandolar.leadmanagement.exception.DuplicateConversionException;
import com.pandolar.leadmanagement.exception.ResourceNotFoundException;
import com.pandolar.leadmanagement.mapper.CustomerMapper;
import com.pandolar.leadmanagement.mapper.LeadMapper;
import com.pandolar.leadmanagement.repository.CustomerRepository;
import com.pandolar.leadmanagement.repository.LeadRepository;
import com.pandolar.leadmanagement.service.LeadService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class LeadServiceImpl implements LeadService {

    private static final Logger log = LoggerFactory.getLogger(LeadServiceImpl.class);

    private final LeadRepository leadRepository;
    private final CustomerRepository customerRepository;
    private final LeadMapper leadMapper;
    private final CustomerMapper customerMapper;

    public LeadServiceImpl(LeadRepository leadRepository,
                           CustomerRepository customerRepository,
                           LeadMapper leadMapper,
                           CustomerMapper customerMapper) {
        this.leadRepository = leadRepository;
        this.customerRepository = customerRepository;
        this.leadMapper = leadMapper;
        this.customerMapper = customerMapper;
    }

    @Override
    @Transactional
    public LeadResponse createLead(CreateLeadRequest request) {
        Lead lead = leadMapper.toEntity(request);
        Lead saved = leadRepository.save(lead);
        log.info("Lead created: id={}, email={}", saved.getId(), saved.getEmail());
        return leadMapper.toResponse(saved);
    }

    @Override
    @Transactional
    public LeadResponse updateLead(UUID id, UpdateLeadRequest request) {
        Lead lead = findLeadOrThrow(id);

        if (request.status() == LeadStatus.CONVERTED) {
            throw new IllegalArgumentException(ErrorMessages.CANNOT_SET_CONVERTED);
        }

        leadMapper.updateEntity(lead, request);
        Lead saved = leadRepository.save(lead);
        log.info("Lead updated: id={}", saved.getId());
        return leadMapper.toResponse(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public LeadResponse getLeadById(UUID id) {
        Lead lead = findLeadOrThrow(id);
        return leadMapper.toResponse(lead);
    }

    @Override
    @Transactional(readOnly = true)
    public List<LeadResponse> getAllLeads() {
        return leadRepository.findAll().stream()
                .map(leadMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public CustomerResponse convertLead(UUID leadId) {
        Lead lead = findLeadOrThrow(leadId);

        if (lead.getStatus() == LeadStatus.CONVERTED) {
            throw new DuplicateConversionException(String.format(ErrorMessages.LEAD_ALREADY_CONVERTED, leadId));
        }

        Customer customer = customerMapper.fromLead(lead);

        lead.setStatus(LeadStatus.CONVERTED);
        leadRepository.save(lead);
        Customer savedCustomer = customerRepository.save(customer);

        log.info("Lead converted to customer: leadId={}, customerId={}", leadId, savedCustomer.getId());
        return customerMapper.toResponse(savedCustomer);
    }

    private Lead findLeadOrThrow(UUID id) {
        return leadRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(ErrorMessages.LEAD_NOT_FOUND + id));
    }
}
