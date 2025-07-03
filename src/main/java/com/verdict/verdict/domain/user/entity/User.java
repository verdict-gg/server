package com.verdict.verdict.domain.user.entity;

import com.verdict.verdict.global.entities.BaseEntity;
import com.verdict.verdict.global.entities.UserRole;
import com.verdict.verdict.global.oAuth.ProviderInfo;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

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
    private UserRole userRole;

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
    public User(ProviderInfo providerInfo, String identifier, String email, UserRole userRole, String nickname, String information, String imageUrl) {
        this.providerInfo = providerInfo;
        this.identifier = identifier;
        this.email = email;
        this.userRole = userRole;
        this.nickname = nickname;
        this.information = information;
        this.imageUrl = imageUrl;
    }

    public void updateRole(UserRole userRole) {
        this.userRole = userRole;
    }

    public void updateUser(String nickname, String email) {
        this.email = email; // email 필드 업데이트
        this.nickname = nickname;
        this.imageUrl = imageUrl;
    }



    public boolean isRegistered() {
       return this.userRole != UserRole.NOT_REGISTERED;
    }
}
