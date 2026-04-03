package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.CompanyResponse;
import com.roomnexus.backend.dto.CreateCompanyRequest;
import com.roomnexus.backend.service.CompanyService;
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
public class CompanyController {

    private final CompanyService companyService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public CompanyResponse createCompany(@RequestBody @Valid CreateCompanyRequest request) {
        return companyService.createCompany(request);
    }

    @GetMapping
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public List<CompanyResponse> getAllCompanies() {
        return companyService.getAllCompanies();
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public CompanyResponse getCompanyById(@PathVariable UUID id) {
        return companyService.getCompanyById(id);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteCompany(@PathVariable UUID id) {
        companyService.deleteCompany(id);
    }
}
