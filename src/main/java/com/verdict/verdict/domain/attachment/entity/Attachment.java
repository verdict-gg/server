package com.verdict.verdict.domain.attachment.entity;

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
public class Attachment extends BaseEntity {

    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(ConstraintMode.NO_CONSTRAINT))
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    private String fileKey;

    private String thumbnailUrl;

    private String originalName;

    private Boolean isVideo;

    @Enumerated(EnumType.STRING)
    private AttachmentStatus attachmentStatus;

    private Long size;

    @Builder
    public Attachment(Long id, Boolean isVideo, String originalName, Post post, Long size, String thumbnailUrl, String fileKey, AttachmentStatus attachmentStatus) {
        this.id = id;
        this.isVideo = isVideo;
        this.originalName = originalName;
        this.post = post;
        this.size = size;
        this.thumbnailUrl = thumbnailUrl;
        this.fileKey = fileKey;
        this.attachmentStatus = attachmentStatus;
    }

    public void updateStatus(AttachmentStatus status){
        this.attachmentStatus = status;
    }
}
