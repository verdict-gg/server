package com.verdict.verdict.domain.attachment.service;

import com.verdict.verdict.domain.S3VideoService;
import com.verdict.verdict.domain.attachment.AttachmentRepository;
import com.verdict.verdict.domain.attachment.dto.VideoResponse;
import com.verdict.verdict.domain.attachment.dto.VideoUploadInitResponse;
import com.verdict.verdict.domain.attachment.entity.Attachment;
import com.verdict.verdict.domain.attachment.entity.AttachmentStatus;
import com.verdict.verdict.domain.attachment.video.VideoUploadCompletedRequest;
import com.verdict.verdict.domain.attachment.video.VideoUploadInitRequest;
import com.verdict.verdict.global.exception.attachment.AttachmentNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class VideoService {

    private final S3VideoService s3VideoService;
    private final AttachmentRepository attachmentRepository;

    // 멀티파트 업로드 초기화 및 Pre-signed URL 목록 생성
    @Transactional
    public VideoUploadInitResponse initiateVideoUpload(VideoUploadInitRequest request) {
        // 멀티파트 업로드 초기화 및 Pre-signed URL 목록 생성
        VideoUploadInitResponse response = s3VideoService.initiateMultipartUpload(request);

        Attachment attachment = Attachment.builder()
                .originalName(request.getFileName())
                .fileKey(response.getFileKey())
                .size(request.getFileSize())
                .isVideo(Boolean.TRUE)
                .attachmentStatus(AttachmentStatus.UPLOADING)
                .build();

        attachmentRepository.save(attachment);

        return response;
    }

    // 멀티파트 업로드 완료 처리
    @Transactional
    public VideoResponse completeVideoUpload(VideoUploadCompletedRequest request) {
        // 멀티파트 업로드 완료 처리
        String url = s3VideoService.completeMultipartUpload(request);
        //TODO: AttachmentNotFoundException
        Attachment attachment = attachmentRepository.findByFileKey(request.getFileKey()).orElseThrow(AttachmentNotFoundException::new);

        attachment.updateStatus(AttachmentStatus.UPLOADED);

        return VideoResponse.of(url);
    }

    // 멀티파트 업로드 중단 처리
    @Transactional
    public void abortVideoUpload(String fileKey, String uploadId) {
        s3VideoService.abortMultipartUpload(fileKey, uploadId);

        Attachment attachment = attachmentRepository.findByFileKey(fileKey).orElseThrow(AttachmentNotFoundException::new);
        attachment.updateStatus(AttachmentStatus.FAILED);
    }

    // S3 비디오 삭제 처리
    @Transactional
    public void deleteVideo(String fileKey) {
        Attachment attachment = attachmentRepository.findByFileKey(fileKey).orElseThrow(AttachmentNotFoundException::new);

        s3VideoService.deleteVideo(fileKey);

        attachmentRepository.delete(attachment);
    }

}
