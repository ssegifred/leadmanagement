package com.pandolar.leadmanagement.mapper;

import com.pandolar.leadmanagement.dto.response.CustomerResponse;
import com.pandolar.leadmanagement.entity.Customer;
import com.pandolar.leadmanagement.entity.Lead;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class CustomerMapper {

    public Customer fromLead(Lead lead) {
        return Customer.builder()
                .name(lead.getName())
                .email(lead.getEmail())
                .phone(lead.getPhone())
                .lead(lead)
                .convertedAt(LocalDateTime.now())
                .build();
    }

    public CustomerResponse toResponse(Customer customer) {
        return new CustomerResponse(
                customer.getId(),
                customer.getName(),
                customer.getEmail(),
                customer.getPhone(),
                customer.getLead().getId(),
                customer.getConvertedAt(),
                customer.getCreatedAt()
        );
    }
}
