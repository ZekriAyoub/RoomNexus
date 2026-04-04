package com.roomnexus.backend.repository;

import com.roomnexus.backend.entity.UserProfile;
import com.roomnexus.backend.entity.UserStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UserProfileRepository extends JpaRepository<UserProfile, UUID> {

    Optional<UserProfile> findByKeycloakUserId(String keycloakUserId);

    List<UserProfile> findByCompanyId(UUID companyId);
    List<UserProfile> findByCompanyIdAndStatus(UUID companyId, UserStatus status);


}
