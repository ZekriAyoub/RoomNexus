package com.roomnexus.backend.dto;

import jakarta.validation.constraints.NotBlank;

public record CreateCompanyRequest(
        @NotBlank String name,
        @NotBlank String keycloakGroupId
) {}