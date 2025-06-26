package com.verdict.verdict.entity;

import jakarta.persistence.*;
import lombok.*;

/* User 엔티티는 사용자의 정보를 저장하는 엔티티입니다.
 * 이메일, 비밀번호, 이미지 URL, 역할, 프로바이더 정보 등을 포함합니다.
 * OAuth2 인증을 통해 사용자를 관리하며, 각 필드는 적절한 제약 조건을 가집니다.
 * oauth api 제공 정보는 이름,메일,프사만 받음.
 */
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    private String password; // aouth만 쓸거라 없애도될듯

    @Column(name = "image_url",length = 1000)
    private String imageUrl;

    @Enumerated(EnumType.STRING)
    private Role role;
    // client name >> naver,google
    private String provider;

    // client id >> naver,google
    private String providerId;


    @Builder
    public User(String email, Long id, String imageUrl, String password, Role role, String provider, String providerId) {
        this.email = email;
        this.id = id;
        this.imageUrl = imageUrl;
        this.password = password; // aouth만 쓸거라 없애도될듯
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
