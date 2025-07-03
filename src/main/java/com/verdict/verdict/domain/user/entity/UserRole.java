package com.verdict.verdict.global.entities;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum UserRole {
    NOT_REGISTERED("ROLE_NOT_REGISTERED","외원가입 이전 사용자"),
    USER("ROLE_USER","일반 사용자"),
    ADMIN("ROLE_ADMIN","관리자");
    private final String key;
    private final String title;
}
