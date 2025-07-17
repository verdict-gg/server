package com.verdict.verdict.global.oAuth.dto;

import com.verdict.verdict.domain.user.dto.AuthResponse;
import com.verdict.verdict.global.entities.UserRole;
import jakarta.validation.constraints.NotNull;

public record GuestResponse(
        String identifier,
        UserRole userRole
//        Integer frameId
) {

    public static GuestResponse from(@NotNull AuthResponse authResponse, @NotNull String identifier) {
        return new GuestResponse(identifier, authResponse.userRole());
    }
}
