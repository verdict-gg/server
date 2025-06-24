package com.verdict.verdict.entity;

import java.util.Arrays;

public enum Status {
    PROGRESS, COMPLETED;

    public static Status of(String status) {
        return Arrays.stream(Status.values())
                .filter(r -> r.name().equalsIgnoreCase(status))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("유효하지 않은 Status"));
    }
}
