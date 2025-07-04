package com.verdict.verdict.global.info.impl;

import com.verdict.verdict.global.constants.ProviderInfo;
import com.verdict.verdict.global.info.OAuth2UserInfo;

import java.util.Map;

public class GoogleOAuth2UserInfo extends OAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {
        super(attributes);
    }

    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getProviderCode());
    }

    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getIdentifier());
    }
}