package com.verdict.verdict.global.config;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;

@Configuration
public class RedisConfig {


    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory);

        // 직렬화 설정 (TokenService의 RedisTemplate<String, Object>와 일치해야 함)
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class)); // 또는 new GenericJackson2JsonRedisSerializer()
        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
        redisTemplate.setHashValueSerializer(new Jackson2JsonRedisSerializer<>(Object.class));

        redisTemplate.afterPropertiesSet();
        return redisTemplate;
    }
}
//
//    @Value("${spring.data.redis.host}")
//    private String redisHost;
//
//    @Value("${spring.data.redis.port}")
//    private int redisPort;
//
//    @Value("${spring.data.redis.password}")
//    private String redisPassword; // 비밀번호 설정 시 추가
//
//    /**
//     * RedisConnectionFactory는 Redis 서버와의 연결을 생성하는 데 사용됩니다.
//     * Lettuce는 비동기 및 반응형 프로그래밍을 지원하는 Redis 클라이언트입니다.
//     */
//    @Bean
//    public RedisConnectionFactory redisConnectionFactory() {
//        LettuceConnectionFactory lettuceConnectionFactory = new LettuceConnectionFactory(redisHost, redisPort);
//        if (redisPassword != null && !redisPassword.isEmpty()) {
//            lettuceConnectionFactory.setPassword(redisPassword);
//        }
//        return lettuceConnectionFactory;
//    }
//
//    /**
//     * RedisTemplate은 Redis 데이터베이스에 데이터를 저장하고 조회하는 데 사용됩니다.
//     * key와 value의 직렬화 방식을 설정합니다.
//     */
//    @Bean
//    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory redisConnectionFactory) {
//        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
//        redisTemplate.setConnectionFactory(redisConnectionFactory);
//
//        // Key Serializer: String (사람이 읽기 쉽게)
//        redisTemplate.setKeySerializer(new StringRedisSerializer());
//
//        // Value Serializer: JSON (객체를 JSON 문자열로 변환하여 저장)
//        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        // Hash Key Serializer: String
//        redisTemplate.setHashKeySerializer(new StringRedisSerializer());
//
//        // Hash Value Serializer: JSON
//        redisTemplate.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
//
//        redisTemplate.afterPropertiesSet();
//        return redisTemplate;
//    }
//}