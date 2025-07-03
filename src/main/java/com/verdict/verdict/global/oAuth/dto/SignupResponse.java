package com.verdict.verdict.global.oAuth.dto;



public record SignupResponse(
        Long userId,
        String identifier
) {

    public static SignupResponse of(Long userId, String identifier) {
        return new SignupResponse(userId, identifier);
    }
}