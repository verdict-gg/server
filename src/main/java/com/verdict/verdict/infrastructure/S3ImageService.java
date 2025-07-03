package com.verdict.verdict.infrastructure;

import com.verdict.verdict.dto.ImageFile;
import com.verdict.verdict.dto.ImageUploadResult;
import com.verdict.verdict.exception.image.ImageException;
import io.awspring.cloud.s3.ObjectMetadata;
import io.awspring.cloud.s3.S3Resource;
import io.awspring.cloud.s3.S3Template;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;

@RequiredArgsConstructor
@Component
public class S3ImageService {

    private final S3Template s3Template;

    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    private static final String IMAGE_UPLOAD_PATH = "images/";

    public ImageUploadResult uploadImage(ImageFile imageFile) {
         String fileKey = IMAGE_UPLOAD_PATH + imageFile.getUniqueName();

        try (InputStream inputStream = imageFile.getInputStream()) {
            // contentType 등은 ObjectMetadataBuilder로 지정 가능
            S3Resource upload = s3Template.upload(
                    bucket,
                    fileKey,
                    inputStream,
                    ObjectMetadata.builder()
                            .contentType(imageFile.getContentType())
                            .contentLength(imageFile.getSize())
                            .build()
            );
            String url = upload.getURL().toString();

            return new ImageUploadResult(url, fileKey);
        } catch (IOException | RuntimeException e) {
            throw new ImageException("이미지 업로드에 실패했습니다. " + e.getMessage());
        }
    }

    public void deleteImage(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) return;

        try {
            s3Template.deleteObject(bucket, fileKey);
        } catch (RuntimeException e) {
            throw new ImageException("이미지 삭제에 실패했습니다. " + e.getMessage());
        }
    }
}
