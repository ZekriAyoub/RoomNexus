package com.roomnexus.backend.service;

import com.roomnexus.backend.dto.CreateUserProfileRequest;
import com.roomnexus.backend.dto.UserProfileResponse;
import com.roomnexus.backend.entity.Company;
import com.roomnexus.backend.entity.Role;
import com.roomnexus.backend.entity.UserProfile;
import com.roomnexus.backend.entity.UserStatus;
import com.roomnexus.backend.repository.CompanyRepository;
import com.roomnexus.backend.repository.UserProfileRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserProfileRepository userProfileRepository;

    private final CompanyRepository companyRepository;

    // Appelé au premier login — crée le profil si inexistant
    public UserProfileResponse getOrCreateUserProfile(Jwt jwt,
                                                      CreateUserProfileRequest request) {
        String keycloakUserId = jwt.getSubject();

        return userProfileRepository.findByKeycloakUserId(keycloakUserId)
                .map(this::toResponse)
                .orElseGet(() -> createUserProfile(keycloakUserId, jwt, request));
    }

    private UserProfileResponse createUserProfile(String keycloakUserId,
                                                  Jwt jwt,
                                                  CreateUserProfileRequest request) {
        Company company = companyRepository.findById(request.companyId())
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + request.companyId()));

        UserProfile profile = new UserProfile();
        profile.setKeycloakUserId(keycloakUserId);
        profile.setFirstName(jwt.getClaimAsString("given_name"));
        profile.setLastName(jwt.getClaimAsString("family_name"));
        profile.setCompany(company);
        profile.setRole(extractRole(jwt));
        profile.setStatus(UserStatus.PENDING);

        return toResponse(userProfileRepository.save(profile));
    }

    private Role extractRole(Jwt jwt) {
        Map<String, Object> realmAccess = jwt.getClaim("realm_access");
        if (realmAccess == null) return Role.USER;

        List<String> roles = (List<String>) realmAccess.get("roles");
        if (roles == null) return Role.USER;

        if (roles.contains("SUPER_ADMIN")) return Role.SUPER_ADMIN;
        if (roles.contains("ADMIN"))       return Role.ADMIN;
        return Role.USER;
    }

    public UserProfileResponse getMyProfile(Jwt jwt) {
        String keycloakUserId = jwt.getSubject();
        UserProfile profile = userProfileRepository
                .findByKeycloakUserId(keycloakUserId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Profile not found for user: " + keycloakUserId));
        return toResponse(profile);
    }

    public List<UserProfileResponse> getUsersByCompany(UUID companyId) {
        companyRepository.findById(companyId)
                .orElseThrow(() -> new EntityNotFoundException(
                        "Company not found with id: " + companyId));

        return userProfileRepository.findByCompanyId(companyId)
                .stream()
                .map(this::toResponse)
                .toList();
    }

    private UserProfileResponse toResponse(UserProfile profile) {
        return new UserProfileResponse(
                profile.getId(),
                profile.getKeycloakUserId(),
                profile.getFirstName(),
                profile.getLastName(),
                profile.getCompany().getId(),
                profile.getRole(),
                profile.getStatus(),
                profile.getCreatedAt()
        );
    }

    public UserProfileResponse approveUser(UUID id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "UserProfile not found: " + id));
        profile.setStatus(UserStatus.ACTIVE);
        return toResponse(userProfileRepository.save(profile));
    }

    public UserProfileResponse rejectUser(UUID id) {
        UserProfile profile = userProfileRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException(
                        "UserProfile not found: " + id));
        profile.setStatus(UserStatus.REJECTED);
        return toResponse(userProfileRepository.save(profile));
    }

    public List<UserProfileResponse> getPendingUsers(UUID companyId) {
        return userProfileRepository.findByCompanyIdAndStatus(companyId, UserStatus.PENDING)
                .stream()
                .map(this::toResponse)
                .toList();
    }
}
