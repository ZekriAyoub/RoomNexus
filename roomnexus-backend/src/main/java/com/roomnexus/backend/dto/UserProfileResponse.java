package com.roomnexus.backend.dto;

import com.roomnexus.backend.entity.Role;
import com.roomnexus.backend.entity.UserStatus;

import java.time.LocalDateTime;
import java.util.UUID;

public record UserProfileResponse(
        UUID id,
        String keycloakUserId,
        String firstName,
        String lastName,
        UUID companyId,
        Role role,
        UserStatus status,
        LocalDateTime createdAt
) {
}
