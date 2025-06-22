package com.verdict.verdict.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import static org.springframework.security.config.Customizer.withDefaults;


@Configuration
@Slf4j
@EnableWebSecurity
@EnableWebMvc
public class WebSecurityConfig implements WebMvcConfigurer {

    @Value("${DEV_SERVER_URL}")
    private String frontServerUrl;
    @Value("${NAVER_CLIENT_ID:}")
    private String naverClientId;
    @Value("${NAVER_CLIENT_SECRET:}")
    private String naverClient;
    @Value("${GOOGLE_CLIENT_ID:}")
    private String googleClientId;
    @Value("${GOOGLE_CLIENT_SECRET:}")
    private String googleClientSecret;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/**", "/swagger-ui/**", "/v3/api-docs/**", "/oauth2/**").permitAll()
                        .anyRequest().permitAll()
                )
//               .oauth2Login(withDefaults())
                .cors(withDefaults());
        return http.build();
    }


    // CORS 설정
    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("http://localhost:3000")
                .allowedMethods("GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS")
                .allowCredentials(true);
    }

}



