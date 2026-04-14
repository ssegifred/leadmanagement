package com.pandolar.leadmanagement.repository;

import com.pandolar.leadmanagement.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, UUID> {

    boolean existsByLeadId(UUID leadId);

    Optional<Customer> findByLeadId(UUID leadId);
}
