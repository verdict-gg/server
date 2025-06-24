package com.verdict.verdict.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password;

    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;
    // provider
    private String provider;

    // providerId : oauth 로그인 한 유저의 고유 ID
    private String providerId;


    @Builder
    public User(String email, Long id, String imageUrl, String password, Role role, String provider, String providerId) {
        this.email = email;
        this.id = id;
        this.imageUrl = imageUrl;
        this.password = password;
        this.role = role;
        this.provider = "google"; // 임시로 google만.
        this.providerId = String.valueOf(id); // providerId는 id로 설정
    }
}
