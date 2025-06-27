package com.verdict.verdict.dto.oauth2;

import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.Map;

@Getter
@ToString
@Builder
public class OAuth2UserInfo {
    private String name;
    private String email;
    private String imageUrl;
    private String provider;
    private String providerId;

    public static OAuth2UserInfo of(String provider, Map<String, Object> attributes) {
        switch (provider) {
            case "google":
                return ofGoogle(attributes);
            case "naver":
                return ofNaver(attributes);
            default:
                throw new RuntimeException("@@@OAUTH2 User INFO 에러@@@ " + provider);
        }
    }

    public static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
//                .providerId("google_" + (String) attributes.get("sub"))
                .providerId((String) attributes.get("sub"))
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .build();
    }

    public static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Object response = attributes.get("response");
        if (!(response instanceof Map)) {
            throw new IllegalStateException("Naver response is not a map");
        }
        Map<String, Object> responseMap = (Map<String, Object>) response;
        return OAuth2UserInfo.builder()
                .provider("naver")
                .providerId((String) responseMap.get("id"))
                .email((String) responseMap.get("email"))
                .name((String) responseMap.get("name"))
                .imageUrl((String) responseMap.get("profile_image"))
                .build();
    }
    public User toEntity() {
        return User.builder()
                .email(email)
                .imageUrl(imageUrl)
                .role(Role.ROLE_USER)
                .provider(provider)
                .providerId(providerId)
                .build();
    }
}
