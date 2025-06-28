package com.verdict.verdict.entity;

import java.util.Arrays;

public enum AttachmentStatus {
    UPLOADING, UPLOADED, FAILED;

    public static AttachmentStatus of(String status) {
        return Arrays.stream(AttachmentStatus.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Status"));
    }
}
