package com.verdict.verdict.global.service;


import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
public class TokenService {

    private final RedisTemplate<String, Object> redisTemplate;

    public TokenService(RedisTemplate<String, Object> redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * 리프레시 토큰을 Redis에 저장하고 만료 시간을 설정합니다.
     * @param identifier 사용자 ID (Key)
     * @param refreshToken 리프레시 토큰 (Value)
     * @param expirationSeconds 만료 시간 (초)
     */
    public void save(String identifier, String refreshToken, long expirationSeconds) { // JwtFacadeService.generateRefreshToken과 일치하도록 save 메서드 시그니처 변경
        redisTemplate.opsForValue().set(identifier, refreshToken, Duration.ofSeconds(expirationSeconds));
        // 또는 redisTemplate.opsForValue().set(userId, refreshToken, expirationSeconds, TimeUnit.SECONDS);
    }

    /**
     * 사용자 ID로 리프레시 토큰을 조회합니다.
     * @param identifier 사용자 ID
     * @return 리프레시 토큰 (없으면 null)
     */
    public String findByIdentifier(String identifier) { // 기존 findByIdentifier 대신 getRefreshToken과 동일한 기능
        Object token = redisTemplate.opsForValue().get(identifier);
        return token != null ? token.toString() : null;
    }

    /**
     * 리프레시 토큰을 삭제하여 무효화합니다 (로그아웃 시).
     * @param identifier 사용자 ID
     */
    public void deleteRefreshToken(String identifier) {
        redisTemplate.delete(identifier); // 키(userId)를 사용하여 데이터 삭제
    }

    /**
     * 리프레시 토큰의 존재 여부 확인
     * @param identifier 사용자 ID
     * @return 존재 여부
     */
    public boolean isRefreshHijacked(String identifier, String providedToken) { // JwtFacadeService.validateRefreshToken과 일치
        String storedToken = findByIdentifier(identifier);
        // 1. Redis에 토큰이 없거나 (로그아웃되었거나 만료됨)
        // 2. Redis에 저장된 토큰과 제공된 토큰이 다르면 (하이재킹 또는 새로운 토큰 발급 후 이전 토큰 사용 시도)
        return storedToken == null || !storedToken.equals(providedToken);
    }
}