package com.verdict.verdict.global.dto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApiResponse<T> {

    private int code;
    private HttpStatus status;
    private Object message;
    private T data;

    public ApiResponse(int code, HttpStatus status, Object message, T data) {
        this.code = code;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public ApiResponse(HttpStatus status, Object message, T data) {
        this.code = status.value();
        this.status = status;
        this.data = data;
        this.message = message;
    }

    public static <T> ApiResponse<T> of(int code, HttpStatus status, Object message, T data) {
        return new ApiResponse<>(code, status, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, Object message, T data) {
        return new ApiResponse<>(httpStatus, message, data);
    }

    public static <T> ApiResponse<T> of(HttpStatus httpStatus, T data) {
        return of(httpStatus, httpStatus.name(), data);
    }

    public static <T> ApiResponse<T> ok(T data) {
        return of(HttpStatus.OK, data);
    }
}
