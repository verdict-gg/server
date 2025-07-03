package com.verdict.verdict.domain.attachment.service;

import com.verdict.verdict.domain.S3ImageService;
import com.verdict.verdict.domain.attachment.AttachmentRepository;
import com.verdict.verdict.domain.attachment.dto.ImageResponse;
import com.verdict.verdict.domain.attachment.entity.Attachment;
import com.verdict.verdict.domain.attachment.image.ImageFile;
import com.verdict.verdict.domain.attachment.image.ImageUploadResult;
import com.verdict.verdict.global.exception.image.ImageException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class ImageService {
    private final S3ImageService s3ImageService;
    private final AttachmentRepository attachmentRepository;

    @Transactional
    public ImageResponse save(MultipartFile image) {
        validateSizeOfImage(image);

        ImageFile imageFile = new ImageFile(image);
        ImageUploadResult result = s3ImageService.uploadImage(imageFile);

        Attachment attachment = Attachment.builder()
                .fileKey(result.getFileKey())
                .isVideo(Boolean.FALSE)
                .size(image.getSize())
                .originalName(image.getOriginalFilename())
                .build();

        attachmentRepository.save(attachment);

        return ImageResponse.of(result.getImageUrl());
    }

    public void deleteImage(String originalUrl) {
        s3ImageService.deleteImage(originalUrl);
    }

    private void validateSizeOfImage(MultipartFile image) {
        if (image == null || image.isEmpty()) {
            throw new ImageException("이미지가 존재하지 않습니다.");
        }
    }
}

