package com.verdict.verdict.config;

import com.verdict.verdict.handler.OAuth2FailureHandler;
import com.verdict.verdict.handler.OAuth2SuccessHandler;
import com.verdict.verdict.service.CustomOAuth2UserService;
import com.verdict.verdict.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsUtils;

@Configuration
//@Order(1)
@RequiredArgsConstructor
@EnableWebSecurity
@Slf4j
public class WebSecurityConfig {
    public static final String PERMITTED_URI[] = {"/v3/**", "/swagger-ui/**", "/api/auth/**", "/login",
            "/favicon.ico"};
    private static final String PERMITTED_ROLES[] = {"USER", "ADMIN"};
    private final CustomCorsConfigSource customCorsConfigSource;
    private final CustomOAuth2UserService customOAuth2UserService;
    private final UserService userService;
    private final OAuth2SuccessHandler oauth2SuccessHandler;
    private final OAuth2FailureHandler oauth2FailureHandler;
//    @Value("${front-server.url}")
//    private String frontServerUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .cors(cors -> cors.configurationSource(customCorsConfigSource))
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request -> request
                                .requestMatchers("/api/admin/**").hasRole("ADMIN")
                                .requestMatchers(CorsUtils::isPreFlightRequest).permitAll() // 이거 커스텀해서 로컬 포트로 리스폰
                                .requestMatchers(PERMITTED_URI).permitAll()
                                .anyRequest().hasAnyRole(PERMITTED_ROLES)
//                        .anyRequest().permitAll()
                )
                .oauth2Login(oauth2Custom -> oauth2Custom
                        .successHandler(oauth2SuccessHandler)
                        .failureHandler(oauth2FailureHandler)
                        .userInfoEndpoint(endpointConfig
                                -> endpointConfig.userService(customOAuth2UserService)
                        )
                )
                .logout(logout -> logout
                        .logoutUrl("/api/logout").permitAll()
                );
        return http.build();
    }
}

