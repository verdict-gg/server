package com.verdict.verdict.global.oAuth.info.impl;

import com.verdict.verdict.global.oAuth.info.OAuth2UserInfo;

import java.util.Map;

public class NaverOAuth2UserInfo extends OAuth2UserInfo {

    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super(extractResponse(attributes));
    }

    private static Map<String, Object> extractResponse(Map<String, Object> attributes) {
        // Naver's OAuth2 response is wrapped in a "response" key
        Object response = attributes.get("response");
        if (response instanceof Map) {
            return (Map<String, Object>) response;
        }
            throw new IllegalArgumentException("Invalid Naver OAuth2 response format");
        }

    @Override
    public String getProviderCode() {
        return "naver";
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get("email"); // "id"
    }
}