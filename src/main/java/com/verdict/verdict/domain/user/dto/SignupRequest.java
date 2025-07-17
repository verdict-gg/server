package com.verdict.verdict.domain.user.dto;

import lombok.Builder;

@Builder
public record SignupRequest(
        String identifier,
        String nickname
//        List<String> information // tier , PS , vote , comments, posts ...
//        String information
) {

}