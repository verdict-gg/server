package com.verdict.verdict.global.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum SuccessCode {

    // 200 OK
    SUCCESS(HttpStatus.OK, " 요청이 정상적으로 처리되었습니다."),
    JOIN_SUCCESS(HttpStatus.OK, " 정상적으로 처리되었습니다."),
    QUIT_SUCCESS(HttpStatus.OK, " 취소가 정상적으로 처리되었습니다."),

    // 201 CREATED
    CREATED(HttpStatus.CREATED, " 정상적으로 생성되었습니다.");


    private final HttpStatus status;
    private final String message;
}