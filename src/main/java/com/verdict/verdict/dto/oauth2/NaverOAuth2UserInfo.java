package com.verdict.verdict.dto.oauth2;


import java.util.Map;

public class NaverOAuth2UserInfo extends AbstractOAuth2UserInfo {


    public NaverOAuth2UserInfo(Map<String, Object> attributes) {
        super((Map<String, Object>) attributes.get(ProviderInfo.NAVER.getAttributeKey()));
    }
    /**
     * @return "naver"
     */
    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.NAVER.getProviderCode());
    }

    /**
     * @return "id"
     */
    @Override
    public String getUserIdentifier() {
        return (String) attributes.get(ProviderInfo.NAVER.getIdentifier());
    }
}
