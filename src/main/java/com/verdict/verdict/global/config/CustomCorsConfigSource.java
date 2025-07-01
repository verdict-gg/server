package com.verdict.verdict.global.config;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import java.util.Collections;
import java.util.List;

@Component
public class CustomCorsConfigSource implements CorsConfigurationSource {
    private final String ALLOWED_ORIGIN;
    private final List<String> ALLOWED_METHOD = List.of("GET", "POST", "PUT", "DELETE", "OPTIONS");

    // 임시 : 로컬 yml에서 추출
    public  CustomCorsConfigSource(@Value("${url.front}") String BASE_URL) {
        ALLOWED_ORIGIN = BASE_URL;
    }
    @Override
    public CorsConfiguration getCorsConfiguration(HttpServletRequest request) {
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowedOrigins(Collections.singletonList(ALLOWED_ORIGIN));
        config.setAllowedMethods(ALLOWED_METHOD);
        config.setAllowCredentials(true);
        config.setAllowedHeaders(Collections.singletonList("*"));
        config.setMaxAge(3600L);
        config.setAllowPrivateNetwork(true); // 이거로 서버-> 고쿠로컬로 리스폰
        return config;
    }
}
