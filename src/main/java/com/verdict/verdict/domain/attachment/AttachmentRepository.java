package com.verdict.verdict.domain.attachment;

import com.verdict.verdict.domain.attachment.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByFileKey(String fileKey);
}
