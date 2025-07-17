package com.verdict.verdict.global.oAuth.info.impl;

import com.verdict.verdict.global.oAuth.info.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderCode() {
        return "google";
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get("sub");
    }

    public String getGoogleId() {
        return (String) attributes.get("email"); // "id"
    }
}