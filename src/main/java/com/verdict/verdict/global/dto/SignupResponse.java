package com.verdict.verdict.global.dto;


public record SignupResponse(
        Long userId,
        String identifier
) {

    public static SignupResponse of(Long userId, String identifier) {
        return new SignupResponse(userId, identifier);
    }
}