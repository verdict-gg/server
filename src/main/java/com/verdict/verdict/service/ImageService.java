package com.verdict.verdict.service;

import com.verdict.verdict.dto.ImageFile;
import com.verdict.verdict.dto.ImageUploadResult;
import com.verdict.verdict.dto.response.ImageResponse;
import com.verdict.verdict.entity.Attachment;
import com.verdict.verdict.exception.image.ImageException;
import com.verdict.verdict.infrastructure.S3ImageService;
import com.verdict.verdict.repository.AttachmentRepository;
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

