package com.verdict.verdict.global.info;

import com.verdict.verdict.global.constants.ProviderInfo;
import com.verdict.verdict.global.info.impl.GoogleOAuth2UserInfo;
import com.verdict.verdict.global.info.impl.NaverOAuth2UserInfo;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;


public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth(ProviderInfo providerInfo, Map<String, Object> attributes) {
        switch (providerInfo) {
            case GOOGLE -> {
                return new GoogleOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                return new NaverOAuth2UserInfo(attributes);
            }
            case LOL -> {
                return null;
            }
        }
        throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
    }
}






