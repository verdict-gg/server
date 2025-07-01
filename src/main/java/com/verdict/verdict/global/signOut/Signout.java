package com.verdict.verdict.global.signOut;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
@Table(name = "signout")
public class Signout {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "signout_id")
    private Long id;

    private String identifier;

    private String reason;

    @CreatedDate
    @Column(name = "signout_at", updatable = false)
    private LocalDateTime signoutDate;

    @Builder
    public Signout(String identifier, String reason, LocalDateTime signoutDate) {
        this.identifier = identifier;
        this.reason = reason;
        this.signoutDate = signoutDate;
    }
}