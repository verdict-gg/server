package com.verdict.verdict.global.dto;


import com.verdict.verdict.entity.Role;

public record AuthResponse(
        Role role
) {
}