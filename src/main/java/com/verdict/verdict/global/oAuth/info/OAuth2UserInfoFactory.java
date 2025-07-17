package com.verdict.verdict.global.oAuth.info;

import com.verdict.verdict.global.oAuth.ProviderInfo;
import com.verdict.verdict.global.oAuth.info.impl.GoogleOAuth2UserInfo;
import com.verdict.verdict.global.oAuth.info.impl.NaverOAuth2UserInfo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;

import java.util.Map;

@Slf4j
public class OAuth2UserInfoFactory {
    public static OAuth2UserInfo getOAuth(ProviderInfo providerInfo, Map<String, Object> attributes) {
        switch (providerInfo) {
            case GOOGLE -> {
                log.info("GOOGLE OAuth2 User Info Factory called");
                return new GoogleOAuth2UserInfo(attributes);
            }
            case NAVER -> {
                log.info("NAVER OAuth2 User Info Factory called");
                return new NaverOAuth2UserInfo(attributes);
            }
            case LOL -> {
                return null;
            }
        }
        throw new OAuth2AuthenticationException("INVALID PROVIDER TYPE");
    }
}






