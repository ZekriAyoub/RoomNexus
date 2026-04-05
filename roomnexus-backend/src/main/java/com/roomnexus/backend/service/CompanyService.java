package com.roomnexus.backend.service;

import com.roomnexus.backend.dto.CompanyResponse;
import com.roomnexus.backend.dto.CreateCompanyRequest;
import com.roomnexus.backend.entity.Company;
import com.roomnexus.backend.repository.CompanyRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
@Slf4j
public class CompanyService {

    private final CompanyRepository companyRepository;

    public CompanyResponse createCompany(CreateCompanyRequest request) {
        Company company = new Company();
        company.setName(request.name());
        company.setKeycloakGroupId(request.keycloakGroupId());

        Company saved = companyRepository.save(company);
        log.info("Company created: id={} name={}", saved.getId(), saved.getName());
        return toResponse(saved);
    }

    @Transactional(readOnly = true)
    public List<CompanyResponse> getAllCompanies() {
        return companyRepository.findAll()
                .stream()
                .map(this::toResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public CompanyResponse getCompanyById(UUID id) {
        Company company = companyRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + id));
        return toResponse(company);
    }

    private CompanyResponse toResponse(Company company) {
        return new CompanyResponse(
                company.getId(),
                company.getName(),
                company.getKeycloakGroupId()
        );
    }

    public void deleteCompany(UUID id) {
        if (!companyRepository.existsById(id)) {
            throw new EntityNotFoundException("Company not found with id: " + id);
        }
        companyRepository.deleteById(id);
        log.info("Company deleted: id={}", id);
    }
}
