package com.verdict.verdict.exception.video;

import com.verdict.verdict.exception.BadRequestException;

public class VideoException extends BadRequestException {
    private static final String MESSAGE = "이미지 관련 문제가 발생했습니다.";

    public VideoException() {super(MESSAGE);}

    public VideoException(String message) {super(message);}
}
