package com.verdict.verdict.dto.oauth2;

import com.verdict.verdict.entity.Role;
import com.verdict.verdict.entity.User;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

public class UserWithSignupStatusTest {

    @Test
    void testGetName() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();
        UserWithSignupStatus userWithSignupStatus = new UserWithSignupStatus(
                user,
                true,
                Map.of("email", "test@example.com"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "email"
        );

        // When
        String name = userWithSignupStatus.getName();

        // Then
        assertThat(name).isEqualTo("test@example.com");
    }

    @Test
    void testGetAuthorities() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();
        UserWithSignupStatus userWithSignupStatus = new UserWithSignupStatus(
                user,
                true,
                Map.of("email", "test@example.com"),
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "email"
        );

        // When
        var authorities = userWithSignupStatus.getAuthorities();

        // Then
        assertThat(authorities).hasSize(1);
        assertThat(authorities.iterator().next().getAuthority()).isEqualTo("ROLE_USER");
    }

    @Test
    void testGetAttributes() {
        // Given
        User user = User.builder()
                .email("test@example.com")
                .role(Role.ROLE_USER)
                .build();
        Map<String, Object> attributes = Map.of("email", "test@example.com");
        UserWithSignupStatus userWithSignupStatus = new UserWithSignupStatus(
                user,
                true,
                attributes,
                Collections.singleton(new SimpleGrantedAuthority("ROLE_USER")),
                "email"
        );

        // When
        Map<String, Object> resultAttributes = userWithSignupStatus.getAttributes();

        // Then
        assertThat(resultAttributes).isEqualTo(attributes);
    }
}