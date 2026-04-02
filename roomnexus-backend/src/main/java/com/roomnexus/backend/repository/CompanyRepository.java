package com.roomnexus.backend.repository;

import com.roomnexus.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface CompanyRepository extends JpaRepository<Company, UUID> {

    Optional<Company> findByKeycloakGroupId(String keycloakGroupId);

}
