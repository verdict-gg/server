package com.verdict.verdict.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VideoUploadInitRequest {
    private String fileName;
    private String contentType;
    private Long fileSize;
}
