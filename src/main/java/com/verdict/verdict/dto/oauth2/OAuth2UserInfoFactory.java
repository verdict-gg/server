package com.verdict.verdict.dto.oauth2;

import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;


public class OAuth2UserInfoFactory {
    public static AbstractOAuth2UserInfo of(ProviderInfo providerInfo, Map<String, Object> attribute) {
        switch (providerInfo) {
            case GOOGLE: return new GoogleOAuth2UserInfo(attribute);
            case NAVER: return new NaverOAuth2UserInfo(attribute);
            default: throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
        }
    }
}
