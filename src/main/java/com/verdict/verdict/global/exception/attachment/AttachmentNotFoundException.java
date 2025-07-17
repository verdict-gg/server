package com.verdict.verdict.global.exception.attachment;

import com.verdict.verdict.global.exception.NotFoundException;

public class AttachmentNotFoundException extends NotFoundException {
    private static final String MESSAGE = "해당 첨부파일을 찾을 수 없습니다.";

    public AttachmentNotFoundException() {super(MESSAGE);}

    public AttachmentNotFoundException(String message) {super(message);}
}
