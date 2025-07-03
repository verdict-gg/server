package com.verdict.verdict.controller;


import com.verdict.verdict.dto.request.VideoAbortRequest;
import com.verdict.verdict.dto.request.VideoUploadCompletedRequest;
import com.verdict.verdict.dto.request.VideoUploadInitRequest;
import com.verdict.verdict.dto.response.ImageResponse;
import com.verdict.verdict.dto.response.VideoResponse;
import com.verdict.verdict.dto.response.VideoUploadInitResponse;
import com.verdict.verdict.global.dto.ApiResponse;
import com.verdict.verdict.service.ImageService;
import com.verdict.verdict.service.VideoService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@RequestMapping("/api/attachments")
@RestController
public class AttachmentController {

    private final ImageService imageService;
    private final VideoService videoService;

    @Operation(summary = "S3에 이미지 업로드")
    @PostMapping("/images")
    public ResponseEntity<ApiResponse<ImageResponse>> uploadImage(@RequestPart MultipartFile image) {
        ImageResponse response = imageService.save(image);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    //TODO: 게시글 생성 시 findByFileKey() 를 사용하여 DB Attachment 테이블에 저장해야함.

    @Operation(summary = "S3에 영상 멀티파트 업로드 초기화 및 Pre-signed URL 목록 생성")
    @PostMapping("/videos/init")
    public ResponseEntity<ApiResponse<VideoUploadInitResponse>> initVideoUpload(@RequestBody VideoUploadInitRequest request) {
        VideoUploadInitResponse response = videoService.initiateVideoUpload(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "S3 영상 멀티파트 업로드 완료")
    @PostMapping("/videos/complete")
    public ResponseEntity<ApiResponse<VideoResponse>> completeVideoUpload(@RequestBody VideoUploadCompletedRequest request) {
        VideoResponse response = videoService.completeVideoUpload(request);
        return ResponseEntity.ok(ApiResponse.ok(response));
    }

    @Operation(summary = "S3 영상 멀티파트 업로드 중단")
    @PostMapping("/videos/abort")
    public ResponseEntity<ApiResponse<Void>> abortVideoUpload(@RequestBody VideoAbortRequest request) {
        videoService.abortVideoUpload(request.getFileKey(), request.getUploadId());
        return ResponseEntity.ok(ApiResponse.ok(null));
    }

    @Operation(summary = "S3 영상 삭제")
    @DeleteMapping("/videos/{fileKey}")
    public ResponseEntity<ApiResponse<Void>> deleteVideo(@PathVariable String fileKey) {
        videoService.deleteVideo(fileKey);
        return ResponseEntity.ok(ApiResponse.ok(null));
    }
}
