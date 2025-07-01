package com.verdict.verdict.global.constants;

import java.util.Arrays;

public enum ProviderType {
    LOL,
    NAVER,
    GOOGLE;

    public static ProviderType from(String provider) {
        String upperCastedProvider = provider.toUpperCase();

        return Arrays.stream(ProviderType.values())
                .filter(item -> item.name().equals(upperCastedProvider))
                .findFirst()
                .orElseThrow();
    }
}