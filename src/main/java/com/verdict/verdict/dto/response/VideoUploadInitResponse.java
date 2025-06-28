package com.verdict.verdict.dto.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class VideoUploadInitResponse {
    private String uploadId;
    private String fileKey;
    private List<String> presignedUrls;
    private int partCount;
    private long partSize;

    public static VideoUploadInitResponse of(String uploadId, String fileKey, List<String> presignedUrls, int partCount, long partSize) {
        return new VideoUploadInitResponse(uploadId, fileKey, presignedUrls, partCount, partSize);
    }
}
