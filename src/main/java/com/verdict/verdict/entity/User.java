package com.verdict.verdict.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Setter
@Entity
public class User extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String email;

    @NotNull
    private String identifier;

    @Column(name = "image_url",length = 1000)
    private String imageUrl;

    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;

    private String provider;

    private String providerId;


    @Builder
    public User(String email,String identifier, String imageUrl, Role role, String provider, String providerId) {
        this.email = email;
        this.identifier = identifier;
        this.imageUrl = imageUrl;
        this.role = role;
        this.provider = provider;
        this.providerId = providerId;
    }
}
