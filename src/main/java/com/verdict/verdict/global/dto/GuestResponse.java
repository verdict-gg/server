package com.verdict.verdict.global.dto;

import com.verdict.verdict.entity.Role;
import jakarta.validation.constraints.NotNull;

public record GuestResponse(
        String identifier,
        Role role
//        Integer frameId
) {

    public static GuestResponse from(@NotNull AuthResponse authResponse, @NotNull String identifier) {
        return new GuestResponse(identifier, authResponse.role());
    }
}
