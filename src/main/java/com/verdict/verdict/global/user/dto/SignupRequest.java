package com.verdict.verdict.global.user.dto;

import lombok.Builder;

@Builder
public record SignupRequest(
        String identifier,
        String nickname
//        List<String> information // tier , PS , vote , comments, posts ...
//        String information
) {

}