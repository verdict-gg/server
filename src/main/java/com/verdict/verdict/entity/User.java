package com.verdict.verdict.entity;

import com.verdict.verdict.dto.oauth2.ProviderInfo;
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

    @Column(unique = true, length = 20)
    private String nickname;

    @Column(unique = true, length = 100)
    private String information;

    @NotNull
    @Column(name = "identifier",length = 1000)
    private String identifier;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ProviderInfo providerInfo;

    @Column(name = "image_url",length = 1000)
    private String imageUrl;

    @NotNull()
    @Enumerated(EnumType.STRING)
    @Column(length = 100)
    private Role role;


//    private String vote;

    @Builder
    public User(ProviderInfo providerInfo, String identifier, Role role, String nickname, String information
                ) {
        this.providerInfo = providerInfo;
        this.identifier = identifier;
        this.role = role;
        this.nickname = nickname;
        this.information = information;

    }
    /*========= Additional Service ===========*/

    private String providerId;
    public boolean isRegistered() {
        return this.role != Role.NOT_REGISTERED;
    }

    public void updateUser(String nickname, String information, String imageUrl) {
        this.nickname = nickname;
        this.information = information;
        this.imageUrl = imageUrl;
    }
}
