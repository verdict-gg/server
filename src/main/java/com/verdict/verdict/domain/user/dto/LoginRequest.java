package com.verdict.verdict.domain.user.dto;

import jakarta.validation.constraints.NotEmpty;

public record LoginRequest(
        @NotEmpty
        String id
) {
}