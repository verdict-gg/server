package com.verdict.verdict.global.exception;

public interface MessageSender {
    /**
     * @param exception 발생한 예외
     */
    void sendMessage(Exception exception);
}