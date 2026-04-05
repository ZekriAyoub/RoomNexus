package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.CreateUserProfileRequest;
import com.roomnexus.backend.dto.UserProfileResponse;
import com.roomnexus.backend.entity.Role;
import com.roomnexus.backend.service.UserProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/users")
@RequiredArgsConstructor
@Tag(name = "Users", description = "User profile management")
public class UserProfileController {

    private final UserProfileService userProfileService;

    @Operation(summary = "Get or create my profile",
            description = "Creates a user profile on first login, returns existing profile otherwise")
    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse getOrCreateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateUserProfileRequest request) {
        return userProfileService.getOrCreateUserProfile(jwt, request);
    }

    @Operation(summary = "Get my profile")
    @GetMapping("/me")
    public UserProfileResponse getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return userProfileService.getMyProfile(jwt);
    }

    @Operation(summary = "Get all users by company")
    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getUsersByCompany(@PathVariable UUID companyId) {
        return userProfileService.getUsersByCompany(companyId);
    }

    @Operation(summary = "Get pending users by company")
    @GetMapping("/company/{companyId}/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getPendingUsers(@PathVariable UUID companyId) {
        return userProfileService.getPendingUsers(companyId);
    }

    @Operation(summary = "Approve a user",
            description = "Sets the user status to ACTIVE")
    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse approveUser(@PathVariable UUID id) {
        return userProfileService.approveUser(id);
    }

    @Operation(summary = "Reject a user",
            description = "Sets the user status to REJECTED")
    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse rejectUser(@PathVariable UUID id) {
        return userProfileService.rejectUser(id);
    }

    @Operation(summary = "Update user role",
            description = "Updates the application role of a user. Restricted to SUPER_ADMIN")
    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UserProfileResponse updateRole(
            @PathVariable UUID id,
            @RequestParam Role role) {
        return userProfileService.updateRole(id, role);
    }
}