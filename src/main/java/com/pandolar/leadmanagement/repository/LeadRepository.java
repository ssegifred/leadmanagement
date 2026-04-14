package com.pandolar.leadmanagement.repository;

import com.pandolar.leadmanagement.entity.Lead;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface LeadRepository extends JpaRepository<Lead, UUID> {

    boolean existsByEmail(String email);
}
