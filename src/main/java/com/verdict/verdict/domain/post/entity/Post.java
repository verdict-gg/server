package com.verdict.verdict.domain.post.entity;

import com.verdict.verdict.domain.user.entity.Status;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Post extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    private String title;

    private String content;

    @Enumerated(EnumType.STRING)
    private Status status;

    private LocalDateTime closedAt;

    @Builder
    public Post(LocalDateTime closedAt, String content, Long id, Status status, String title, User user) {
        this.closedAt = closedAt;
        this.content = content;
        this.id = id;
        this.status = status;
        this.title = title;
        this.user = user;
    }
}
