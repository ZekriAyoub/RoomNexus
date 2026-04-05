package com.roomnexus.backend.exception;

import com.roomnexus.backend.entity.UserProfile;

import java.util.UUID;

public final class CompanyAccessGuard {

    private CompanyAccessGuard() {}

    public static void verify(UserProfile user, UUID targetCompanyId) {
        if (!user.getCompany().getId().equals(targetCompanyId)) {
            throw new CompanyAccessException();
        }
    }
}