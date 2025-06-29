package com.verdict.verdict.dto.oauth2;

import java.util.Map;

public class GoogleOAuth2UserInfo extends AbstractOAuth2UserInfo {

    public GoogleOAuth2UserInfo(Map<String, Object> attributes) {

        super(attributes);
    }
    /**
     * @return "google"
     */
    @Override
    public String getProviderCode() {
        return (String) attributes.get(ProviderInfo.GOOGLE.getProviderCode());
    }

    /**
     * @return "sub"
     */
    @Override
    public String getUserIdentifier() {

        return (String) attributes.get(ProviderInfo.GOOGLE.getIdentifier());
    }
}
