package com.verdict.verdict.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {

    @Value("${spring.data.redis.host}")
    private String redisHost;

    @Value("${spring.data.redis.port}")
    private int redisPort;

    @Value("${spring.data.redis.password}")
    private String redisPassword; // 비밀번호 설정 시 추가

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
        if (redisPassword != null && !redisPassword.isEmpty()) {
            lettuceConnectionFactory.setPassword(redisPassword);
        }
        return lettuceConnectionFactory;
    }
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {

        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);
        // 직렬화 설정 (TokenService의 RedisTemplate<String, Object>와 일치해야 함)
        // Key Serializer: String (사람이 읽기 쉽게)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Value Serializer: JSON (객체를 JSON 문자열로 변환하여 저장)
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // 또는 new GenericJackson2JsonRedisSerializer()
        // Hash Key Serializer: String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        // Hash Value Serializer: JSON
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // 또는 new GenericJackson2JsonRedisSerializer()
        redisTemplate.setHashKeySerializer(new StringRedisSerializer()); // Hash Key Serializer: String
        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
//    /**
//     * RedisConnectionFactory는 Redis 서버와의 연결을 생성하는 데 사용됩니다.
//     * Lettuce는 비동기 및 반응형 프로그래밍을 지원하는 Redis 클라이언트입니다.
//     */
