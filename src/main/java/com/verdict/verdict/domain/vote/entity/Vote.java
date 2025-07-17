package com.verdict.verdict.domain.vote.entity;

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
public class Vote extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private User user;

    @JoinColumn(name = "vote_option_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private VoteOption voteOption;

    @Builder
    public Vote(Long id, User user, VoteOption voteOption) {
        this.id = id;
        this.user = user;
        this.voteOption = voteOption;
    }
}
