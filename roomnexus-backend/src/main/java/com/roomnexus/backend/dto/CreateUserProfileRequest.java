package com.roomnexus.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateUserProfileRequest(
        @NotNull UUID companyId
        ) {
}
