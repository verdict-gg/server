package com.verdict.verdict.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Getter
public class VideoAbortRequest {
    private String uploadId;
    private String fileKey;
}
