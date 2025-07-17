package com.verdict.verdict.domain.vote.entity;

import com.verdict.verdict.domain.post.entity.Post;
import com.verdict.verdict.global.entities.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class VoteOption extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String name;

    private Integer count;

    @Builder
    public VoteOption(Integer count, Long id, String name, Post post) {
        this.count = count;
        this.id = id;
        this.name = name;
        this.post = post;
    }
}
