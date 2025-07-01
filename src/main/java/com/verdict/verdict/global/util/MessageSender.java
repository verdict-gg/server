package com.verdict.verdict.global.util;

public interface MessageSender {
    /**
     * @param exception 발생한 예외
     */
    void sendMessage(Exception exception);
}