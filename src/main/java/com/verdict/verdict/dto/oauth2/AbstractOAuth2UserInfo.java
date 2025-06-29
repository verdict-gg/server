package com.verdict.verdict.dto.oauth2;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public abstract class AbstractOAuth2UserInfo {

    public abstract String getProviderCode();
    public abstract String getUserIdentifier();
    protected Map<String, Object> attributes;
}