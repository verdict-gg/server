package com.verdict.verdict.domain.user.dto;


import com.verdict.verdict.global.entities.UserRole;

public record AuthResponse(
        UserRole userRole
) {
}