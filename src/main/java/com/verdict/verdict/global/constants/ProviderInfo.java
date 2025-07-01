package com.verdict.verdict.global.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@RequiredArgsConstructor
@Getter
public enum ProviderInfo {

    LOL("lol", "account", "account#${ccTLD}"+"${num}"),
    NAVER("response", "id", "email"),
    GOOGLE("sub", "sub", "email");

    private final String providerCode;
    private final String attributeKey;
    private final String identifier;

    public static ProviderInfo from(String provider) {
        String upperCastedProvider = provider.toUpperCase();

        return Arrays.stream(ProviderInfo.values())
                .filter(item -> item.name().equals(upperCastedProvider))
                .findFirst()
                .orElseThrow();
    }
}