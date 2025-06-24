package com.verdict.verdict.controller;


import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class TestComponent {

    @Value("${GOOGLE_CLIENT_ID}")
    private String googleClientId;
    @Value("${GOOGLE_CLIENT_SECRET}")
    private String googleClientSecret;
    @Value("${NAVER_CLIENT_ID}")
    private String naverClientId;
    @Value("${NAVER_CLIENT_SECRET}")
    private String naverClientSecret;
    @Value("${DEV_CLIENT_URL}")
    private String frontServerUrl;


    public void EnvInfo() {
        log.info("연결된 프론트 서버 : {}", frontServerUrl);
    }


    public void init() {
        if (googleClientId.isEmpty() && googleClientSecret.isEmpty() && naverClientId.isEmpty() && naverClientSecret.isEmpty()) {
            log.warn("GOOGLE_CLIENT_ID: {}, GOOGLE_CLIENT_SECRET: {}, NAVER_CLIENT_ID: {}, NAVER_CLIENT_SECRET: {}", googleClientId, googleClientSecret, naverClientId, naverClientSecret);
        } else {
            EnvInfo();
        }

    }
}
