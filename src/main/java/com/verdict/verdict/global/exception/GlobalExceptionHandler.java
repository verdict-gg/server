package com.verdict.verdict.global.exception;


import com.verdict.verdict.global.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.List;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<ApiResponse<Object>> handleBadRequestException(BadRequestException e) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 Bad Request

        return ResponseEntity.status(status).body(ApiResponse.of(status, e.getMessage(), null));
    }

    // 인증되지 않았을 때
    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<Object>> handleAuthenticationException(UnauthorizedException e) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.UNAUTHORIZED; // 401 Unauthorized

        return ResponseEntity.status(status).body(ApiResponse.of(status, e.getMessage(), null));
    }

    // 인증은 되었지만, 해당 자원에 대한 접근 권한이 없을 때
    @ExceptionHandler(ForbiddenException.class)
    public ResponseEntity<ApiResponse<Object>> handleForbiddenException(ForbiddenException e) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.FORBIDDEN; // 403 Forbidden

        return ResponseEntity.status(status).body(ApiResponse.of(status, e.getMessage(), null));
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleNotFoundException(NotFoundException e) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.NOT_FOUND; // 404 Not Found

        return ResponseEntity.status(status).body(ApiResponse.of(status, e.getMessage(), null));
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiResponse<Object>> handleConflictException(ConflictException e) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.CONFLICT; // 409 Conflict

        return ResponseEntity.status(status).body(ApiResponse.of(status, e.getMessage(), null));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleException(Exception e) {
        log.error("Unhandled exception", e);

        HttpStatus status = HttpStatus.INTERNAL_SERVER_ERROR; // 500 Internal Server Error

        return ResponseEntity.status(status).body(ApiResponse.of(status, e.getMessage(), null));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Object>> handleValidationException(MethodArgumentNotValidException e) {
        log.error(e.getMessage());

        BindingResult bindingResult = e.getBindingResult();

        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 Bad Request

        List<String> errorMessages = bindingResult.getFieldErrors().stream()
                .map(fieldError -> String.format("[%s](은)는 %s 입력된 값: [%s]",
                        fieldError.getField(),
                        fieldError.getDefaultMessage(),
                        fieldError.getRejectedValue()))
                .toList();
        return ResponseEntity.status(status).body(ApiResponse.of(status, errorMessages, null));
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    public ResponseEntity<ApiResponse<Object>> handleMissingParam(MissingServletRequestParameterException e) {
        log.error(e.getMessage());

        HttpStatus status = HttpStatus.BAD_REQUEST; // 400 Bad Request

        List<String> errorMessages = List.of(e.getMessage());

        return ResponseEntity.status(status).body(ApiResponse.of(status, errorMessages, null));
    }
}
