package com.verdict.verdict.domain;

import com.verdict.verdict.domain.attachment.dto.VideoUploadInitResponse;
import com.verdict.verdict.domain.attachment.video.VideoUploadCompletedRequest;
import com.verdict.verdict.domain.attachment.video.VideoUploadInitRequest;
import com.verdict.verdict.global.exception.video.VideoException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.PresignedUploadPartRequest;
import software.amazon.awssdk.services.s3.presigner.model.UploadPartPresignRequest;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Component
public class S3VideoService {

    private static final long PART_SIZE = 6 * 1024 * 1024; // 6mb
    private static final String VIDEO_UPLOAD_PATH = "videos/";
    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    @Value("${spring.cloud.aws.s3.bucket}")
    private String bucket;

    // 멀티파트 업로드 초기화 및 Pre-signed URL 목록 생성
    public VideoUploadInitResponse initiateMultipartUpload(VideoUploadInitRequest request) {
        try {
            String originalName = request.getFileName();
            String fileKey = VIDEO_UPLOAD_PATH + UUID.randomUUID() + "_" + originalName;

            CreateMultipartUploadRequest createRequest = CreateMultipartUploadRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .contentType(request.getContentType())
                    .build();

            CreateMultipartUploadResponse createResponse = s3Client.createMultipartUpload(createRequest);

            // 파트 개수 계산
            int partCount = (int) Math.ceil((double) request.getFileSize() / PART_SIZE);

            // 각 파트별 Pre-signed URL 생성
            List<String> presignedUrls = new ArrayList<>();
            for (int i = 1; i <= partCount; i++) {
                String url = generatePresignedUrl(fileKey, createResponse.uploadId(), i);
                presignedUrls.add(url);
            }

            return VideoUploadInitResponse.of(
                    createResponse.uploadId(),
                    fileKey,
                    presignedUrls,
                    partCount,
                    PART_SIZE
            );
        } catch (RuntimeException e) {
            throw new VideoException("S3 멀티파트 업로드 초기화에 실패했습니다. " + e.getMessage());
        }
    }

    // 각 파트별 Pre-signed URL 생성
    private String generatePresignedUrl(String fileKey, String uploadId, int partNumber) {
        UploadPartRequest uploadPartRequest = UploadPartRequest.builder()
                .bucket(bucket)
                .key(fileKey)
                .uploadId(uploadId)
                .partNumber(partNumber)
                .build();

        UploadPartPresignRequest presignRequest = UploadPartPresignRequest.builder()
                .signatureDuration(Duration.ofHours(1))
                .uploadPartRequest(uploadPartRequest)
                .build();

        PresignedUploadPartRequest presignedRequest = s3Presigner.presignUploadPart(presignRequest);
        return presignedRequest.url().toString();
    }

    // 멀티파트 업로드 완료
    public String completeMultipartUpload(VideoUploadCompletedRequest request) {
        try {
            List<CompletedPart> parts = request.getCompletedParts().stream()
                    .map(p -> CompletedPart.builder()
                            .partNumber(p.getPartNumber())
                            .eTag(p.getETag())
                            .build())
                    .collect(Collectors.toList());

            CompleteMultipartUploadRequest completeRequest = CompleteMultipartUploadRequest.builder()
                    .bucket(bucket)
                    .key(request.getFileKey())
                    .uploadId(request.getUploadId())
                    .multipartUpload(CompletedMultipartUpload.builder().parts(parts).build())
                    .build();

            CompleteMultipartUploadResponse response = s3Client.completeMultipartUpload(completeRequest);
            return response.location(); // 최종 파일의 S3 URL
        } catch (RuntimeException e) {
            throw new VideoException("멀티파트 업로드 완료에 실패했습니다. " + e.getMessage());
        }
    }

    // 멀티파트 업로드 중단
    public void abortMultipartUpload(String fileKey, String uploadId) {
        try {
            AbortMultipartUploadRequest request = AbortMultipartUploadRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .uploadId(uploadId)
                    .build();
            s3Client.abortMultipartUpload(request);
        } catch (RuntimeException e) {
            throw new VideoException("멀티파트 업로드 중단에 실패했습니다. " + e.getMessage());
        }
    }

    // 파일 삭제
    public void deleteVideo(String fileKey) {
        if (fileKey == null || fileKey.isEmpty()) return;

        try {
            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucket)
                    .key(fileKey)
                    .build();
            s3Client.deleteObject(request);
        }catch (RuntimeException e) {
            throw new VideoException("영상 삭제에 실패했습니다. " + e.getMessage()); //
        }
    }
}
