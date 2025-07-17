package com.verdict.verdict.domain.attachment.video;

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
