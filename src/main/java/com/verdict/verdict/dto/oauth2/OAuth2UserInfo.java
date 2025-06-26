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
    private String id;
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
                throw new RuntimeException("CustomOaut2UserService에서 NameAttributeKey 받는 로직 바꾸면 됨 " + provider);
        }
    }

    public static OAuth2UserInfo ofGoogle(Map<String, Object> attributes) {
        return OAuth2UserInfo.builder()
                .provider("google")
                .id("google_" + (String) attributes.get("sub"))
                .email((String) attributes.get("email"))
                .name((String) attributes.get("name"))
                .imageUrl((String) attributes.get("picture"))
                .build();
    }
    public static OAuth2UserInfo ofNaver(Map<String, Object> attributes) {
        Map<String, Object> response = (Map<String, Object>) attributes.get("response");
        return OAuth2UserInfo.builder()
                .provider("naver")
                .id("naver_"+(String) response.get("id"))
                .email((String) response.get("email"))
                .name((String) response.get("name"))
                .imageUrl((String) response.get("profile_image"))
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
