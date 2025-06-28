package com.verdict.verdict.repository;

import com.verdict.verdict.entity.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttachmentRepository extends JpaRepository<Attachment, Long> {
    Optional<Attachment> findByFileKey(String fileKey);
}
