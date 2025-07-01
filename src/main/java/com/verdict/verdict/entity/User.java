package com.verdict.verdict.entity;

import com.verdict.verdict.global.constants.ProviderInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class User extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull()
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Role role;

    @NotNull
    @Column(name = "identifier", length = 1000, unique = true)
    private String identifier;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProviderInfo providerInfo;

    private String email; // OAuth2 연결 계정

    @Column(unique = true, length = 20)
    private String nickname;

    @Column(length = 100)
    private String information;

    @Column(name = "image_url", length = 1000)
    private String imageUrl;

    //    private String vote;
    /*========= Additional Service ===========*/

    @Builder
    public User(ProviderInfo providerInfo, String identifier, String email, Role role, String nickname, String information, String imageUrl) {
        this.providerInfo = providerInfo;
        this.identifier = identifier;
        this.email = email;
        this.role = role;
        this.nickname = nickname;
        this.information = information;
        this.imageUrl = imageUrl;
    }

    public void updateRole(Role role) {
        this.role = role;
    }

    public void updateUser(String nickname, String email) {
        this.email = email; // email 필드 업데이트
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }



    public boolean isRegistered() {
       return this.role != Role.NOT_REGISTERED;
    }
}
