package com.roomnexus.backend.controller;

import com.roomnexus.backend.dto.CreateUserProfileRequest;
import com.roomnexus.backend.dto.UserProfileResponse;
import com.roomnexus.backend.entity.Role;
import com.roomnexus.backend.service.UserProfileService;
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
public class UserProfileController {

    private final UserProfileService userProfileService;

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public UserProfileResponse getOrCreateProfile(
            @AuthenticationPrincipal Jwt jwt,
            @RequestBody @Valid CreateUserProfileRequest request) {
        return userProfileService.getOrCreateUserProfile(jwt, request);
    }

    @GetMapping("/me")
    public UserProfileResponse getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return userProfileService.getMyProfile(jwt);
    }

    @GetMapping("/company/{companyId}")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getUsersByCompany(
            @PathVariable UUID companyId) {
        return userProfileService.getUsersByCompany(companyId);
    }

    @PatchMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse approveUser(@PathVariable UUID id) {
        return userProfileService.approveUser(id);
    }

    @PatchMapping("/{id}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public UserProfileResponse rejectUser(@PathVariable UUID id) {
        return userProfileService.rejectUser(id);
    }

    @GetMapping("/company/{companyId}/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public List<UserProfileResponse> getPendingUsers(@PathVariable UUID companyId) {
        return userProfileService.getPendingUsers(companyId);
    }

    @PatchMapping("/{id}/role")
    @PreAuthorize("hasRole('SUPER_ADMIN')")
    public UserProfileResponse updateRole(
            @PathVariable UUID id,
            @RequestParam Role role) {
        return userProfileService.updateRole(id, role);
    }
}