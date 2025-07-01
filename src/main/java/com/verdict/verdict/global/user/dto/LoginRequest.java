package com.verdict.verdict.global.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty
        String id
) {
}