package com.verdict.verdict.dto.oauth2;

import lombok.Builder;
import lombok.Getter;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@Getter
@Builder
public class OAuth2UserInfo {

    private String id;
    private String name;
    private String email;
    private String imageUrl;
    private Map<String, Object> attributes;

    public static OAuth2UserInfo of(String registrationId, OAuth2User oAuth2User) {
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return switch (registrationId) {
            case "google" -> ofGoogle(attributes);
            case "naver" -> ofNaver(attributes);
            default -> throw new RuntimeException("OAUTH2ID {}" + registrationId);
        };
    }

    public static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .id((String) attributes.get("sub"))
                .name((String) attributes.get("name"))
                .email((String) attributes.get("email"))
                .imageUrl((String) attributes.get("picture"))
                .attributes(attributes)
                .build();
    }

    public static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .id((String) response.get("id"))
                .name((String) response.get("nickname"))
                .email((String) response.get("email"))
                .imageUrl((String) response.get("profile_image"))
                .attributes(attributes)
                .build();
    }
}