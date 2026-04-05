package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.CompanyResponse;
import com.roomnexus.backend.dto.CreateCompanyRequest;
import com.roomnexus.backend.service.CompanyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/companies")
@RequiredArgsConstructor
@Tag(name = "Companies", description = "Company management")
public class CompanyController {

    private final CompanyService companyService;

    @Operation(summary = "Create a company")
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public CompanyResponse createCompany(@RequestBody @Valid CreateCompanyRequest request) {
        return companyService.createCompany(request);
    }

    @Operation(summary = "Get all companies")
    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<CompanyResponse> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @Operation(summary = "Get company by ID")
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public CompanyResponse getCompanyById(@PathVariable UUID id) {
        return companyService.getCompanyById(id);
    }

    @Operation(summary = "Delete a company",
            description = "Deletes a company and all its associated rooms and users")
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public void deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
    }
}