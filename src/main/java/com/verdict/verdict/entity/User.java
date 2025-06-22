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

    @Builder
    public User(String email, Long id, String imageUrl, String password, Role role) {
        this.email = email;
        this.id = id;
        this.imageUrl = imageUrl;
        this.password = password;
        this.role = role;
    }
}
