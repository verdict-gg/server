package com.verdict.verdict.dto.response;


import org.springframework.http.HttpStatus;

public class BusinessException extends RuntimeException {
    private HttpStatus status;

    public BusinessException() {
        super();
    }

    public BusinessException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.status = errorCode.getStatus();
    }

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(String message, Throwable cause) {
        super(message, cause);
    }

    public BusinessException(Throwable cause) {
        super(cause);
    }
}
