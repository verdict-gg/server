package com.verdict.verdict.domain.comment.entity;

import com.verdict.verdict.domain.post.entity.Post;
import com.verdict.verdict.domain.user.entity.User;
import com.verdict.verdict.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Comment extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "user_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String  content;

    @Builder
    public Comment(String content, Long id, Post post, User user) {
        this.content = content;
        this.id = id;
        this.post = post;
        this.user = user;
    }
}
