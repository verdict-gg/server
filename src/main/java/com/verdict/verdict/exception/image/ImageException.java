package com.verdict.verdict.exception.image;

import com.verdict.verdict.exception.BadRequestException;

public class ImageException extends BadRequestException {
    private static final String MESSAGE = "이미지 관련 문제가 발생했습니다.";

    public ImageException() {super(MESSAGE);}

    public ImageException(String message) {super(message);}
}
